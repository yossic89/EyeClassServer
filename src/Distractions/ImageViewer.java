package Distractions;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;

public class ImageViewer {

    public static void showImage(byte[] byte_img) {
        try
        {
            java.awt.Image img = ImageIO.read(new ByteArrayInputStream(byte_img));
            if (frame == null)
                init();
            if (lbl != null)
                frame.remove(lbl);
            icon = new ImageIcon(img);
            lbl = new JLabel();
            lbl.setIcon(icon);
            frame.add(lbl);
            frame.revalidate();
        }
        catch (Exception e)
        {
            System.out.println("Faild to show image: " + e.getMessage());
        }
    }

    private static void init() {
        frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(700, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    static JFrame frame;
    static JLabel lbl;
    static ImageIcon icon;

}
