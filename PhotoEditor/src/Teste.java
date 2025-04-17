import pt.iscte.greyditor.Editor;
import pt.iscte.greyditor.Greyditor;

public class Teste {
    public static void main(String[] args) {
        Greyditor editor = new Greyditor("Teste");
        //editor.addEffect("Simple Posterize", Teste::simplePosterize);
        editor.addOperation("Cover", Teste::cover);
        editor.addOperation("Duplicate", Teste::duplicate);

        editor.open("monalisa.jpg");
    }

    static void simplePosterize(int[][] image) {
        for (int y = 0; y < image.length; y++) {
            for (int x = 0; x < image[0].length; x++) {
                if (image[y][x] <= 0.5) {
                    image[y][x] = image[y][x] + 50;
                } else {
                    image[y][x] = image[y][x] - 50;
                }
            }

        }

    }

    static int[][] cover(int[][] image, Editor editor) {
        int width = image[0].length;
        int height = image.length;
        int[][] cover = new int[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cover[x][y] = 0;
            }
        }
        return cover;
    }

    static int[][] duplicate(int[][] image, Editor editor) {
        int[][] duplicate = new int[image.length][image[0].length];
        for (int y = 0; y < image.length; y++) {
            for (int x = 0; x < image[0].length; x++) {
                duplicate[y][x] = image[y][x];
            }
        }
        return duplicate;
    }
}
