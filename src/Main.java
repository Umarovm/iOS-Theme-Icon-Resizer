import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class Main {
	public static void main(String[] args) throws IOException {
		final int DIMENSIONS = 256;
		final String EXECUTABLE_NAME = new File(Main.class.getProtectionDomain()
				.getCodeSource()
				.getLocation()
				.getPath())
				.getName();
		final String FILE_FORMAT = "png";
		File[] icons;
		Image newResizedImage;
		String filename;

		if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
			System.out.println("usage: " + EXECUTABLE_NAME.substring(0, EXECUTABLE_NAME.lastIndexOf(".")) + " icon_dir …");
			return;
		}
		final ArrayList<File>[] iconDirs = new ArrayList[args.length];
		for (int i = 0; i < args.length; i++) {
			icons = (new File(args[i])).listFiles();
			if (icons == null) {
				System.out.println(EXECUTABLE_NAME + ": " + args[i] + ": Not a directory");
				return;
			}
			iconDirs[i] = new ArrayList<>(Arrays.asList(icons));
		}
		for (ArrayList<File> iconDir : iconDirs)
			for (File icon : iconDir)
				if (!icon.getName().equals(".DS_Store")) {
					filename = icon.getName();
					if (!filename.substring(filename.lastIndexOf(".") + 1).equals("png")) {
						System.out.println(EXECUTABLE_NAME + ": " + ": Directories must only contain png images");
						return;
					}
					newResizedImage = ImageIO.read(icon).getScaledInstance(DIMENSIONS, DIMENSIONS, Image.SCALE_SMOOTH);
					if (!(newResizedImage instanceof BufferedImage)) {
						// Create a buffered image with transparency
						BufferedImage bi = new BufferedImage(
								newResizedImage.getWidth(null), newResizedImage.getHeight(null),
								BufferedImage.TYPE_INT_ARGB);

						Graphics2D graphics2D = bi.createGraphics();
						graphics2D.drawImage(newResizedImage, 0, 0, null);
						graphics2D.dispose();

						newResizedImage = bi;
					}
					ImageIO.write((BufferedImage) newResizedImage, FILE_FORMAT, icon);
				}
	}
}
