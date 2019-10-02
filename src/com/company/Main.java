package com.company;



import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Timestamp;

public class Main {
	//"C:\\Users\\Sam\\Pictures\\pic.jpg"

    public static void main(String[] args) throws IOException {
    	File image = new File("C:\\Users\\Sam\\Pictures\\field.jpg");
		long startTime = System.nanoTime();
		normalizeImgC(image);
		long endTime = System.nanoTime();
		long finalTime = (endTime - startTime) / 1000000;
		System.out.println("Final Time: " + finalTime);
    }

    public static void normalizeImgC(File imageFile){
        BufferedImage img = null;

        try {
            img = ImageIO.read(imageFile);
			System.out.println("Image Read: Success");

        }catch(IOException e){
            System.out.println(e);
			System.out.println("Image Read: Fail");
        }

		assert img != null;
		double[][] norm = new double[img.getWidth()][img.getHeight()];
        double[][] red = new double[img.getWidth()][img.getHeight()];
		double[][] green = new double[img.getWidth()][img.getHeight()];
		double[][] blue = new double[img.getWidth()][img.getHeight()];

		double sum = 0;
		double redSum = 0;
		double greenSum = 0;
		double blueSum = 0;
		int width = img.getWidth();
		int height = img.getHeight();
		//getting the mean of all the numbers
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int p = img.getRGB(x, y);
				int r = (p>>16)&0xff;
				int g = (p>>8)&0xff;
				int b = p&0xff;
				//img.setRGB(x, y, p);
				redSum += r;
				greenSum += g;
				blueSum += b;
				sum += x + y;
			}
		}
		double avg = sum / (width * height);
		double redAvg = redSum / (width * height);
		double greenAvg = greenSum / (width * height);
		double blueAvg = blueSum / (width * height);
		double sum2 = 0;
		double redSum2 = 0;
		double greenSum2 = 0;
		double blueSum2 = 0;

		//getting the standard deviation
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int p = img.getRGB(x,y);
				int r = (p>>16)&0xff;
				int g = (p>>8)&0xff;
				int b = p&0xff;
				double meanSquare = Math.pow((p-avg), 2);
				double redSquare = Math.pow((r-redAvg), 2);
				double greenSquare = Math.pow((g-greenAvg), 2);
				double blueSquare = Math.pow((b-blueAvg), 2);
				sum2 += meanSquare;
				redSum2 += redSquare;
				greenSum2 += greenSquare;
				blueSum2 += blueSquare;
			}
		}

		double stdev = Math.sqrt(sum2/(width*height));
		double redStDev = Math.sqrt(redSum2/(width*height));
		double greenStDev = Math.sqrt(greenSum2/(width*height));
		double blueStDev = Math.sqrt(blueSum2/(width*height));

		System.out.println("Red:\t"+redStDev+"\nGreen:\t"+greenStDev+"\nBlue:\t"+blueStDev);
		System.out.println("Red:\t"+redAvg+"\nGreen:\t"+greenAvg+"\nBlue:\t"+blueAvg);
		
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int p = img.getRGB(x,y);
				int r = (p>>16)&0xff;
				int g = (p>>8)&0xff;
				int b = p&0xff;
				double redNorm = (r - redAvg)/redStDev;
				red[x][y] = redNorm;
				double greenNorm = (g - greenAvg)/greenStDev;
				green[x][y] = greenNorm;
				double blueNorm = (b - blueAvg)/blueStDev;
				blue[x][y] = blueNorm;
			}
		}
		int tblueMean = 0;
		//TESTING PURPROSES ONLY//
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tblueMean += blue[x][y];
			}
		}
		tblueMean /= (height*width);
		double tBlueMean2 = 0;
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tBlueMean2 += Math.pow((blue[x][y]-tblueMean), 2);
			}
		}
		double tBlueStDev = Math.pow((tBlueMean2/(height*width)), .5);
		System.out.println("tStDev: " + tBlueStDev + "\ntBlueMean: " + tblueMean);

		double mostE = Double.MIN_VALUE;
		double redMostE = Double.MIN_VALUE;
		double greenMostE = Double.MIN_VALUE;
		double blueMostE = Double.MIN_VALUE;


		for(int y = 1; y < height; y++) {
			for (int x = 1; x < width; x++) {
				double p = red[x][y];
				if (Math.abs(p)>redMostE){
					redMostE = Math.abs(p);
				}
			}
		}

		for(int y = 1; y < height; y++) {
			for (int x = 1; x < width; x++) {
				double p = green[x][y];
				if (Math.abs(p)>greenMostE){
					greenMostE = Math.abs(p);
				}
			}
		}

		for(int y = 1; y < height; y++) {
			for (int x = 1; x < width; x++) {
				double p = blue[x][y];
				if (Math.abs(p)>blueMostE){
					blueMostE = Math.abs(p);
				}
			}
		}

		for(int y = 1; y < height; y++) {
			for (int x = 1; x < width; x++) {
				double p = red[x][y];
				double redXDec = p * (127/redMostE) + 128;
                red[x][y] = redXDec;
                //if(Math.random() <= 0.2) System.out.println("Red:\t"+red[x][y]);
			}
		}

		for(int y = 1; y < height; y++) {
			for (int x = 1; x < width; x++) {
				double p = green[x][y];
				double greenXDec = p * (127/greenMostE) + 128;
                green[x][y] = greenXDec;
				if(Math.random() <= 0.2) System.out.println("Green:\t"+green[x][y]);
			}
		}

		for(int y = 1; y < height; y++) {
			for (int x = 1; x < width; x++) {
				double p = blue[x][y];
				double blueXDec = p * (127/blueMostE) + 128;
                blue[x][y] = blueXDec;

            }
		}



		System.out.println("RedMost:\t\t"+redMostE+"\n"+"GreenMost:\t\t"+greenMostE+"\n"+"BlueMost:\t\t"+blueMostE);


		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				int p;
				int r = (int) red[x][y];
				int g = (int) green[x][y];
				int b = (int) blue[x][y];


				p = (0xff<<24) | (r<<16) | (g<<8) | b;
				img.setRGB(x, y, p);
//				System.out.println(Integer.toBinaryString(p));

			}
		}


		try{
            System.out.println("Image Save: Success");
			imageFile = new File("C:\\Users\\Sam\\Pictures\\normalizedImgC.jpg");
			ImageIO.write(img, "jpg", imageFile);
		}catch(IOException e){
            System.out.println("Image Save: Fail");
			System.out.println(e);
		}
    }









    public static void normalizeImgBW(File imageFile){
    	BufferedImage img = null;

		try {
			img = ImageIO.read(imageFile);
		}catch(IOException e){
			System.out.println(e);
		}

		BufferedImage img_gray = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		double[][] norm = new double[img.getWidth()][img.getHeight()];

		double sum = 0;
		int width = img.getWidth();
		int height = img.getHeight();
    	//getting the mean of all the numbers
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int p = img.getRGB(x, y);
				img_gray.setRGB(x, y, p);
				sum += x + y;
			}
		}

		double avg = sum / (width * height);
		double sum2 = 0;
		//getting the standard deviation
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int p = img_gray.getRGB(x,y)&0xff;
				double step2 = Math.pow((p-avg), 2);
				sum2 += step2;
			}
		}
		double stdev = Math.sqrt(sum2/(width*height));

		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int p = img_gray.getRGB(x,y)&0xff;
				double n = (p - avg)/stdev;
				norm[x][y] = n;

			}
		}
		double mostE = Double.MIN_VALUE;

		for(int y = 1; y < height; y++) {
			for (int x = 1; x < width; x++) {
				double p = norm[x][y];
				if (Math.abs(p)>mostE){
					mostE = Math.abs(p);
				}
			}
		}
		for(int y = 1; y < height; y++) {
			for (int x = 1; x < width; x++) {
				double p = norm[x][y];
				double xDec = p * (127/mostE) + 128;
			}
		}
		System.out.println(mostE);

		try{
			imageFile = new File("C:\\1 Grace\\Internship\\normalizedImg.jpg");
			ImageIO.write(img_gray, "jpg", imageFile);
		}catch(IOException e){
			System.out.println(e);
		}
	}
    public static void grayScaleImg(File imageFile){
		BufferedImage img = null;

		try {
			img = ImageIO.read(imageFile);
		}catch(IOException e){
			System.out.println(e);
		}

		int width = img.getWidth();
		int hieght = img.getHeight();

		for(int y = 0; y < hieght; y++) {
			for (int x = 0; x < width; x++) {
				int p = img.getRGB(x, y);
				int a = (p>>24)&0xff;
				int r = (p>>16)&0xff;
				int g = (p>>8)&0xff;
				int b = p&0xff;

				int avg = (r+b+g)/3;

				p = (a<<24) | (avg<<16) | (avg<<8) | avg;
				img.setRGB(x, y, p);
			}
		}
		try{
			imageFile = new File("C:\\1 Grace\\Internship\\grayScalePic.jpg");
			ImageIO.write(img, "jpg", imageFile);
		}catch(IOException e){
			System.out.println(e);
		}
	}
}