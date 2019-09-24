package com.company;

import javafx.beans.binding.DoubleExpression;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
	//"C:\\Users\\Sam\\Pictures\\pic.jpg"

    public static void main(String[] args) throws IOException {
    	File image = new File("C:\\Users\\Sam\\Pictures\\pic.jpg");
		//normalizeImgBW(image);
		normalizeImgC(image);
    }
    public static void normalizeImgC(File imageFile){
        BufferedImage img = null;

        try {
            img = ImageIO.read(imageFile);
        }catch(IOException e){
            System.out.println(e);
        }

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
				img.setRGB(x, y, p);
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
				int p = img.getRGB(x,y)&0xff;
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

		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int p = img.getRGB(x,y)&0xff;
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
				double p = red[x][y];
				if (Math.abs(p)>redMostE){
					redMostE = Math.abs(p);
				}
			}
		}
		for(int y = 1; y < height; y++) {
			for (int x = 1; x < width; x++) {
				double p = red[x][y];
				double redXDec = p * (127/redMostE) + 128;
			}
		}
		for(int y = 1; y < height; y++) {
			for (int x = 1; x < width; x++) {
				double p = green[x][y];
				double greenXDec = p * (127/greenMostE) + 128;
			}
		}
		for(int y = 1; y < height; y++) {
			for (int x = 1; x < width; x++) {
				double p = blue[x][y];
				double blueXDec = p * (127/blueMostE) + 128;
			}
		}
		System.out.println(redMostE+"\n"+greenMostE+"\n"+blueMostE);
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				int p;
				int r = (int) red[x][y];
				int g = (int) green[x][y];
				int b = (int) blue[x][y];


				p = (0xff<<24) | (r<<16) | (g<<8) | b;
				img.setRGB(x, y, p);
				//System.out.println(Integer.toBinaryString(p));

			}
		}


		try{
			imageFile = new File("C:\\Users\\Sam\\Pictures\\normalizedImgC.jpg");
			ImageIO.write(img, "jpg", imageFile);
		}catch(IOException e){
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
			imageFile = new File("C:\\Users\\Sam\\Pictures\\normalizedImg.jpg");
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
			imageFile = new File("C:\\Users\\Sam\\Pictures\\grayScalePic.jpg");
			ImageIO.write(img, "jpg", imageFile);
		}catch(IOException e){
			System.out.println(e);
		}
	}
}