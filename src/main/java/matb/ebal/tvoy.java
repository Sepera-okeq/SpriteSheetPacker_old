package matb.ebal;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class tvoy {
    private static int avg(List<Integer> list) {
        Integer sum = 0;

        if (!list.isEmpty()) {
            for (Integer mark : list) {
                sum += mark;
            }

            return new Double(sum.doubleValue() / list.size()).intValue();
        }

        return sum;
    }

    public static void main(String[] args) throws Exception {
        AtomicInteger images_x = new AtomicInteger(0);
        List<Integer> images_y = new ArrayList<>();

        List<File> images = Arrays.stream(Objects.requireNonNull(new File(".").listFiles())).filter(file -> file.getName().endsWith(".png")).sorted().collect(Collectors.toList());

        images.forEach(file -> {
            try {
                BufferedImage img = ImageIO.read(file);

                System.out.println(String.format("%s: %sx%s", file.getName(), img.getWidth(), img.getHeight()));

                images_x.addAndGet(img.getHeight());
                images_y.add(img.getWidth());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        int x = images_x.get();
        int y = avg(images_y);

        AtomicInteger last_interval = new AtomicInteger(0);

        System.out.println(String.format("%sx%s", x, y));

        BufferedImage st_img = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);

        images.forEach(file -> {
            try {
                BufferedImage img = ImageIO.read(file);

                st_img.getGraphics().drawImage(img, last_interval.get(), 0, null);

                last_interval.set(last_interval.get() + img.getWidth());

                System.out.println(String.format("%s: %sx%s", file.getName(), img.getHeight(), img.getWidth()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        ImageIO.write(st_img,"png", new File("st_img.png"));
    }
}
