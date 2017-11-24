import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.text.*;
import java.text.*;
import java.util.*;
import javax.swing.undo.*;
import javax.swing.event.*;
import java.net.*;

public class PixGrab extends JFrame implements ActionListener  { 
	
	JToolBar jt = new JToolBar();
	JButton jb = new JButton("Grab");
	Image ig,ig1;
	JPanel jp=new JPanel();
	int []pix;	
	int t=0, r=0,b=0,l=0;
	double rx,ry;
	KohonenNetwork net;
	DefaultListModel letterListModel = new DefaultListModel();


	public PixGrab(){
		super("untitled");
		
		getContentPane().add(jp);
		getContentPane().add(jt,BorderLayout.NORTH);
		jt.add(jb);
		jb.addActionListener(this);
	}
	
	public void update(Graphics g){
		paint(g);
	}
	
			public void paintComponents(Graphics g){
				super.paintComponents(g);
			}
			
			public void paint(Graphics g){
				ig = Toolkit.getDefaultToolkit().getImage("test.gif");
				g = jp.getGraphics();
				g.drawImage(ig,0,0,null);
				if(t!=0){
					System.out.println("Drawing");
					System.out.println("l"+l);
					System.out.println("t"+t);
					System.out.println("r-l"+(r-l));
					System.out.println("b-t"+(b-t));
					g.setColor(Color.red);
					g.drawRect(r,b,l-r,t-b);
				}
			}
	public void actionPerformed(ActionEvent e){
		
		try{
		PixelGrabber pg = new PixelGrabber(ig,0,0,45,45,true);
		pg.grabPixels();
		pix = (int [])pg.getPixels();
		
		for(int i=0;i<pix.length;i++){
	if(pix[i] != -1)
				System.out.println("["+i+"]= "+pix[i]);	
		}
		
		for(int y=0;y<45;y++){
			for(int i=0;i<45;i++){
				int j=(y*45)+i;
				if(pix[j] != -1){
					System.out.println("Top pix= "+j);
					System.out.println("Top y= "+y);
					t=y;
					break;
				}
			}
		}
		System.out.println("Top= "+t);
				
		for(int y=44;y>=0;y--){
			for(int i=0;i<45;i++){
				int j=(y*45)+i;
				if(pix[j] != -1){
					System.out.println("bot pix= "+j);
					System.out.println("bot y= "+y);
					b=y;
					break;
				}
			}
		}
		System.out.println("Bottom= "+b);
		
		for(int x=0;x<45;x++){
			for(int i=0;i<45;i++){
				int j=(i*45)+x;
				if(pix[j] != -1){
					System.out.println("left pix= "+j);
					System.out.println("left y= "+x);
					l=x;
					break;
				}
			}
		}
		System.out.println("left= "+l);

	for(int x=44;x>=0;x--){
			for(int i=0;i<45;i++){
				int j=(i*45)+x;
				if(pix[j] != -1){
					System.out.println("rgh pix= "+j);
					System.out.println("rgh y= "+x);
					r=x;
					break;
				}
			}
		}
		System.out.println("rigth= "+r);

		}
		catch(Exception ex){
				System.out.print(ex);
		}
		repaint();
		int dsr=0,dsl=0,dst=0,dsb=0;
		
		dsl = r;
		dst =b;
		dsr =l;
		dsb=t;
		Sample s = new Sample(5,7);
		SampleData d = s.getData();
		rx= (double)(dsr-dsl)/(double)d.getWidth();
    		ry= (double)(dst-dsb)/(double)d.getHeight();
    		for(int y =0;y<d.getHeight();y++){
    			for(int x =0;x<d.getWidth();x++){
    				if(downSampleQuadrant(x,y)){
    					d.setData(x,y,true);
    				}
    				else{
    					d.setData(x,y,false);
    				}
    			}	
    		}
		repaint();	
		

	try {
System.out.println("1");		
       FileReader fl = new FileReader( new File("./sample.dat") );
       BufferedReader br = new BufferedReader(fl);
       String line;
       int i=0;
       letterListModel.clear();
	 while ( (line=br.readLine()) !=null ) {
         SampleData ds =
           new SampleData(line.charAt(0),MainEntry.DOWNSAMPLE_WIDTH,MainEntry.DOWNSAMPLE_HEIGHT); 

         letterListModel.add(i++,ds);
         int idx=2;
         for ( int y=0;y<ds.getHeight();y++ ) {
           for ( int x=0;x<ds.getWidth();x++ ) {
             ds.setData(x,y,line.charAt(idx++)=='1');
           }
         }
       }
	br.close();
       fl.close();
System.out.println("2");       
       int inputNeuron = 7* 5;
       int outputNeuron = letterListModel.size();

       TrainingSet set = new TrainingSet(inputNeuron,outputNeuron);
       set.setTrainingSetCount(letterListModel.size());

System.out.println("3");
       for ( int t=0;t<letterListModel.size();t++ ) {
         int idx=0;
         SampleData ds1 = (SampleData)letterListModel.getElementAt(t);
         for ( int y=0;y<ds1.getHeight();y++ ) {
           for ( int x=0;x<ds1.getWidth();x++ ) {
           	  set.setInput(t,idx++,ds1.getData(x,y)?.5:-.5);
           }
         	}
       	}
System.out.println("4");
MainEntry me = new MainEntry();
System.out.println("41");
       	net = new KohonenNetwork(inputNeuron,outputNeuron,me);
System.out.println("42");
       	net.setTrainingSet(set);
System.out.println("43");
       	net.learn();
System.out.println("44");
       	double input[] = new double[5*7];
     int idx=0;
     SampleData ds2 = s.getData();
     for ( int y=0;y<ds2.getHeight();y++ ) {
       for ( int x=0;x<ds2.getWidth();x++ ) {
       	System.out.println("Bool "+ds2.getData(x,y));
         input[idx++] = ds2.getData(x,y)?.5:-.5;
         System.out.println("Input "+input[idx-1]);
       }
     }
System.out.println("5");
     double normfac[] = new double[1];
     double synth[] = new double[1];
System.out.println("6");
     int best = net.winner ( input , normfac , synth ) ;
     char map[] = mapNeurons();
     JOptionPane.showMessageDialog(this,
                                   "  " + map[best] + "   (Neuron #"
                                   + best + " fired)","That Letter Is",
                                   JOptionPane.PLAIN_MESSAGE);

       	
    	 } catch ( Exception ex ) {
       JOptionPane.showMessageDialog(this,"Error: " + ex,
                                     "Training",
                                     JOptionPane.ERROR_MESSAGE);
     		}
	}



	
	protected boolean downSampleQuadrant(int x,int y)
   {
     int w = 45;
     int startX = (int)(r+(x*rx));
     int startY = (int)(b+(y*ry));
     int endX = (int)(startX + rx);
     int endY = (int)(startY + ry);

     for ( int yy=startY;yy<=endY;yy++ ) {
       for ( int xx=startX;xx<=endX;xx++ ) {
         int loc = xx+(yy*w);

         if ( pix[ loc  ]!= -1 )
           return true;
       }
     }

     return false;
   }
	public static void main(String []a){
		PixGrab p = new PixGrab();
		p.setBounds(20,20,300,300);	
		p.setVisible(true);
	}
	
	char []mapNeurons()
   {
     char map[] = new char[letterListModel.size()];
     double normfac[] = new double[1];
     double synth[] = new double[1];

     for ( int i=0;i<map.length;i++ )
       map[i]='?';
     for ( int i=0;i<letterListModel.size();i++ ) {
       double input[] = new double[5*7];
       int idx=0;
       SampleData ds = (SampleData)letterListModel.getElementAt(i);
       for ( int y=0;y<ds.getHeight();y++ ) {
         for ( int x=0;x<ds.getWidth();x++ ) {
           input[idx++] = ds.getData(x,y)?.5:-.5;
         }
       }

       int best = net.winner ( input , normfac , synth ) ;
       map[best] = ds.getLetter();
     }
     return map;
   }

}