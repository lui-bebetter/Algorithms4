/**********************************************************************
 *FileName: SeamCarver
 *Author:   lui1993
 *Date:     2018/2/1221:29
 *Description:The SeamCarver class represents a data type for solving
  the seam carving problem.
 **********************************************************************/

package algs4.EdgeWeightedDigraph.SeamCarving;

/********************************
 *@author lui1993
 *@create 2018/2/12
 *Description:
 ********************************/
public class SeamCarver {
	private static final double BORDER_ENERGY=1000;
	private Picture picture;//the local copy of the input
	private double [][]energy;//energy[i][j]=the energy of the pixel in col i row j
	private double [] distTo;//distTo[v]=sum of energy of the vertices in the shortest path from top to pixel v%width col v/width row
	private int []edgeTo;//edgeTo[v]=edgeTo value of v%width col v/width row
	//Initialize with a picture data type
	public SeamCarver(Picture picture){
		if(picture==null) throw new IllegalArgumentException("Initializes SeamCarver with null arguments");
		this.picture=new Picture(picture);
		int height=this.picture.height(),width=this.picture.width();
		energy=new double[width][height];
		for (int i = 0; i < height; i++) {//row
			for (int j = 0; j < width; j++) {//col
				energy[j][i]=energy(j,i);
			}
		}
	}

	//return current picture
	public Picture picture(){
		return new Picture(picture);
	}

	//width of current picture
	public int width(){
		return picture.width();
	}

	//height of current picture
	public int height(){
		return picture.height();
	}

	// energy of pixel at column x and row y
	public double energy(int x, int y){
		validatePixel(x,y);
		if(x==0||x==(picture.width()-1)||y==0||y==(picture.height()-1)) return BORDER_ENERGY;
		return Math.sqrt(squareDiff(x+1,y,x-1,y)+squareDiff(x,y+1,x,y-1));
	}

	// sequence of indices for vertical seam
	public int [] findVerticalSeam(){
		distTo=new double[picture.width()*picture.height()];
		edgeTo=new int[picture.width()*picture.height()];
		for (int i = 0; i < height(); i++) {//row
			for (int j = 0; j < width(); j++) {//col
				distTo[i*width()+j]=Double.POSITIVE_INFINITY;
			}
		}
		//Initialize energy of the top border
		for (int i = 0; i < picture.width(); i++) {
			distTo[i]=BORDER_ENERGY;
		}
		//relax all the vertices in the row-major order
		for (int i = 0; i < height(); i++) {//row
			for (int j = 0; j < width(); j++) {//col
				verticalRelax(j,i);
			}
		}
		//find the minimal energy path
		int min=(height()-1)*width();
		double min_energy=distTo[min];
		for (int i = 0; i < picture.width(); i++) {
			if(distTo[(height()-1)*width()+i]<min_energy){
				min_energy=distTo[(height()-1)*width()+i];
				min=(height()-1)*width()+i;
			}
		}

		int [] seam=new int[height()];
		int count=height();
		while(min/width()!=0){
			seam[--count]=min%width();
			min=edgeTo[min];
		}
		seam[--count]=min%width();
		assert count==0;

		validateVerticalSeam(seam);
		return seam;
	}

	// sequence of indices for horizontal seam
	public int [] findHorizontalSeam(){
		distTo=new double[picture.width()*picture.height()];
		edgeTo=new int[picture.width()*picture.height()];
		for (int i = 0; i < picture.width(); i++) {
			for (int j = 0; j < picture.height(); j++) {
				distTo[j*width()+i]=Double.POSITIVE_INFINITY;
			}
		}
		//Initialize energy of the left border
		for (int i = 0; i < picture.height(); i++) {
			distTo[i*width()]=BORDER_ENERGY;
		}
		//relax all the vertices in the col-major order
		for (int i = 0; i < picture.width(); i++) {//col
			for (int j = 0; j < picture.height(); j++) {//row
				horizontalRelax(i,j);
			}
		}
		//find the minimal energy path
		int min=width()-1;
		double min_energy=distTo[min];
		for (int i = 0; i < picture.height(); i++) {
			if(distTo[i*width()+width()-1]<min_energy){
				min_energy=distTo[i*width()+width()-1];
				min=i*width()+width()-1;
			}
		}

		int [] seam=new int[width()];
		int count=width();
		while(min%width()!=0){
			seam[--count]=min/width();
			min=edgeTo[min];
		}
		seam[--count]=min/width();
		assert count==0;

		validateHorizontalSeam(seam);
		return seam;
	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int [] seam){
		validateVerticalSeam(seam);
		if(width()<=1) throw new IllegalArgumentException("this picture has width not more than 1 pixel.");
		//reset the picture
		Picture pic=new Picture(width()-1,height());
		for (int i = 0; i < pic.height(); i++) {//row
			for (int j = 0; j < pic.width(); j++) {//col
				int col=j,row=i;
				if(col>=seam[i]) col=j+1;
				pic.setRGB(j,i,picture.getRGB(col,row));
			}
		}
		picture=pic;

		//reset energy array
		for (int i = 0; i < height(); i++) {//row
			for (int j = 0; j < width(); j++) {//col
				if(j>=seam[i]) {
					energy[j][i] = energy[j + 1][i];
				}
			}
		}

		//reset energy array
		for (int i = 0; i < seam.length; i++) {
			if(seam[i]>0)energy[seam[i]-1][i]=energy(seam[i]-1,i);
			if(seam[i]<width())energy[seam[i]][i]=energy(seam[i],i);
		}
	}

	// remove vertical seam from current picture
	public void removeHorizontalSeam(int [] seam){
		validateHorizontalSeam(seam);
		if(height()<=1) throw new IllegalArgumentException("this picture has height not more than 1 pixel.");
		//reset the picture
		Picture pic=new Picture(width(),height()-1);
		for (int i = 0; i < pic.height(); i++) {//row
			for (int j = 0; j < pic.width(); j++) {//col
				int col=j,row=i;
				if(row>=seam[j]) row=i+1;
				pic.setRGB(j,i,picture.getRGB(col,row));
			}
		}
		picture=pic;

		//reset energy array
		for (int i = 0; i < seam.length; i++) {
			if(seam[i]<height())System.arraycopy(energy[i],seam[i]+1,energy[i],seam[i],height()-seam[i]);
		}

		//reset energy array
		for (int i = 0; i < seam.length; i++) {
			if(seam[i]>0)energy[i][seam[i]-1]=energy(i,seam[i]-1);
			if(seam[i]<height())energy[i][seam[i]]=energy(i,seam[i]);
		}

	}

	//relax all the edges incident from the pixel in colum {@code col} row {@code row}
	private void verticalRelax(int col,int row){
		int width=picture.width(),height=picture.height();
		if(col-1>=0 && col-1<width && row+1>=0 && row+1<height){
			int pos1=row*width+col, pos2=col-1+(row+1)*width;
			if(distTo[pos2]>distTo[pos1]+energy[col-1][row+1]){
				distTo[pos2]=distTo[pos1]+energy[col-1][row+1];
				edgeTo[pos2]=pos1;
			}
		}

		if(col>=0 && col<width && row+1>=0 && row+1<height){
			int pos1=row*width+col, pos2=col+(row+1)*width;
			if(distTo[pos2]>distTo[pos1]+energy[col][row+1]){
				distTo[pos2]=distTo[pos1]+energy[col][row+1];
				edgeTo[pos2]=pos1;
			}
		}

		if(col+1>=0 && col+1<width && row+1>=0 && row+1<height){
			int pos1=row*width+col, pos2=col+1+(row+1)*width;
			if(distTo[pos2]>distTo[pos1]+energy[col+1][row+1]){
				distTo[pos2]=distTo[pos1]+energy[col+1][row+1];
				edgeTo[pos2]=pos1;
			}
		}
	}

	//relax all the edges incident from the pixel in colum {@code col} row {@code row}
	private void horizontalRelax(int col,int row){
		int width=picture.width(),height=picture.height();
		if(col+1>=0 && col+1<width && row+1>=0 && row+1<height){
			int pos1=row*width+col, pos2=col+1+(row+1)*width;
			if(distTo[pos2]>distTo[pos1]+energy[col+1][row+1]){
				distTo[pos2]=distTo[pos1]+energy[col+1][row+1];
				edgeTo[pos2]=pos1;
			}
		}

		if(col+1>=0 && col+1<width && row>=0 && row<height){
			int pos1=row*width+col, pos2=col+1+(row)*width;
			if(distTo[pos2]>distTo[pos1]+energy[col+1][row]){
				distTo[pos2]=distTo[pos1]+energy[col+1][row];
				edgeTo[pos2]=pos1;
			}
		}

		if(col+1>=0 && col+1<width && row-1>=0 && row-1<height){
			int pos1=row*width+col, pos2=col+1+(row-1)*width;
			if(distTo[pos2]>distTo[pos1]+energy[col+1][row-1]){
				distTo[pos2]=distTo[pos1]+energy[col+1][row-1];
				edgeTo[pos2]=pos1;
			}
		}
	}

	//computing the RGB gradient between the two pixel
	private double squareDiff(int col1, int row1, int col2, int row2){
		int rgb1=picture.getRGB(col1,row1),rgb2=picture.getRGB(col2,row2);
		int r1=rgb1 >> 16 & 0xFF,r2=rgb2 >> 16 & 0xFF;
		int g1=rgb1 >> 8 & 0xFF, g2=rgb2 >> 8 & 0xFF;
		int b1=rgb1 >> 0 & 0xFF, b2=rgb2 >> 0 & 0xFF;
		return (r1-r2)*(r1-r2)+(g1-g2)*(g1-g2)+(b1-b2)*(b1-b2);
	}

	private void validatePixel(int col, int row){
		int width=picture.width(),height=picture.height();
		if(col<0||col>=width) throw new IllegalArgumentException("Pixel column " + col + " is not between 0 and " + (width-1));
		if(row<0||row>=height) throw new IllegalArgumentException("Pixel row " + row + " is not between 0 and " + (height-1));
	}

	private void validateVerticalSeam(int [] seam){
		if(seam==null) throw new IllegalArgumentException("calls removeVerticalSeam() with null arguments");
		if(seam.length!=height()) throw new IllegalArgumentException("seam has wrong length.");
		validatePixel(seam[0],0);
		for (int i = 1; i < seam.length; i++) {
			validatePixel(seam[i],i);
			if(Math.abs(seam[i]-seam[i-1])>1) throw new IllegalArgumentException("the array is not a valid seam");
		}
	}

	private void validateHorizontalSeam(int [] seam){
		if(seam==null) throw new IllegalArgumentException("calls removeVerticalSeam() with null arguments");
		if(seam.length!=width()) throw new IllegalArgumentException("seam has wrong length.");
		validatePixel(0,seam[0]);
		for (int i = 1; i < seam.length; i++) {
			validatePixel(i,seam[i]);
			if(Math.abs(seam[i]-seam[i-1])>1) throw new IllegalArgumentException("the array is not a valid seam");
		}
	}
}

