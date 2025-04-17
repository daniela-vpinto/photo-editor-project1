import pt.iscte.greyditor.Editor;
import pt.iscte.greyditor.Greyditor;
import pt.iscte.greyditor.Selection;

public class Main {
    public static void main(String[] args) {
        Greyditor editor = new Greyditor("Project 1");
        // Fase1
        editor.addOperation("Horizontal Mirror", Main::horizontal);
        editor.addOperation("Vertical Mirror", Main::vertical);
        editor.addFilter("Brightness", Main::brightness, -50, 50);
        editor.addFilter("Contrast", Main::contrast, -50, 50);
        editor.addFilter("Grain", Main::grain, 0, 50);
        editor.addEffect("Margin", Main::margin);
        editor.addEffect("Vignette", Main::vignette, -50, 50);
        // Livre + bónus
        editor.addEffect("Pixelate", Main::pixelate, 1, 30);
        editor.addOperation("Invert Colors", Main::invertColors);
        editor.addOperation("Sharp Edges", Main::sharpEdges);

        // Fase2
        editor.addOperation("Crop", Main::crop);
        editor.addOperation("Expand", Main::expand);
        editor.addOperation("Posterize", Main::posterize);
        editor.addOperation("Rotate", Main::rotate);
        editor.addOperation("Blur", Main::blur);
        editor.addOperation("Old Effect", Main::oldEffect);
        editor.addOperation("Retro Effect", Main::retro);

        //Fase3
        editor.addOperation("Copy", Operations::copy);
        editor.addOperation("Cut", Operations::cut);
        editor.addOperation("Paste", Operations::paste);
        editor.addOperation("Undo", Operations::undo);
        editor.open("monalisa.jpg");
    }

    static int[][] horizontal(int[][] image, Editor editor) {
        int[][] h = new int[image.length][image[0].length];
        for (int y = 0; y < image.length; y++)
            for (int x = 0; x < image[0].length; x++)
                h[y][x] = image[image.length - 1 - y][x];
        return h;
    }

    static int[][] vertical(int[][] image, Editor editor) {
        int[][] v = new int[image.length][image[0].length];
        for (int y = 0; y < image.length; y++)
            for (int x = 0; x < image[0].length; x++)
                v[y][x] = image[y][image[x].length - 1 - x];
        return v;
    }

    static int brightness(int a, int b) {
        return Math.max(0, Math.min(255, a + b));
    }

    static int contrast(int a, int b) {
        double i = (100.0 + b) / 100.0;
        int newValue = (int) (128 + (a - 128) * i);
        return Math.max(0, Math.min(255, newValue));
    }

    static int grain(int a, int b) {
        int n = (int) (Math.random() * (2 * b + 1)) - 1;
        return Math.max(0, Math.min(255, a + n));
    }

    static int[][] margin(int[][] image) {
        int marginWidth = 40;
        int color = 255;
        int height = image.length;
        int width = image[0].length;
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                if (y < marginWidth || y >= height - marginWidth || x < marginWidth || x >= width - marginWidth) {
                    image[y][x] = color;
                }
        return image;
    }

    static int[][] vignette(int[][] image, int i) {
        int width = image[0].length;
        int height = image.length;
        int centerX = width / 2;
        int centerY = height / 2;

        double distMax = Math.sqrt(centerX * centerX + centerY * centerY);

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                double dist = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
                double d = dist / distMax;
                int effect = (int) (d * i);

                image[y][x] = Math.max(0, Math.min(255, image[y][x] - effect));
            }
        return image;
    }

    static int[][] pixelate(int[][] image, int pixelSize) {
        int width = image[0].length;
        int height = image.length;

        for (int y = 0; y < height; y += pixelSize) {
            for (int x = 0; x < width; x += pixelSize) {
                int sum = 0;
                int count = 0;

                for (int i = 0; i < pixelSize; i++) {
                    for (int j = 0; j < pixelSize; j++) {
                        int newY = y + i;
                        int newX = x + j;

                        if (newY < height && newX < width) {
                            sum += image[newY][newX];
                            count++;

                        }
                    }
                }
                int avg = count > 0 ? sum / count : 0;

                for (int a = 0; a < pixelSize; a++) {
                    for (int b = 0; b < pixelSize; b++) {
                        int newY = y + a;
                        int newX = x + b;

                        if (newY < height && newX < width) {
                            image[newY][newX] = avg;
                        }
                    }
                }
            }
        }
        return image;
    }
    // Fase 2 //

    static int[][] crop(int[][] image, Editor editor) {
        Selection sel = editor.getSelection();
        if (sel == null)
            return null;
        int h = sel.height();
        int w = sel.width();
        int[][] crop = new int[h][w];
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                crop[y][x] = image[sel.y() + y][sel.x() + x];

        return crop;
    }

    static int[][] expand(int[][] image, Editor editor) {
        int width = image[0].length;
        int height = image.length;
        int n = 50;
        int newWidth = width + n;
        int newHeight = height + n;

        int[][] expand = new int[newHeight][newWidth];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                expand[y][x] = image[y][x];
            }
        }
        return expand;
    }

    static int[][] posterize(int[][] image, Editor editor) {
        int width = image[0].length;
        int height = image.length;
        int levels = 4;

        int[][] posterize = new int[height][width];

        int step = 255 / levels;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = image[y][x];
                int newColor = (color / step) * step;
                posterize[y][x] = Math.max(0, Math.min(255, newColor));
            }
        }
        return posterize;
    }

    static int[][] rotate(int[][] image, Editor editor) {
        int width = image[0].length;
        int height = image.length;
        int[][] rotate = new int[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotate[x][height - y - 1] = image[y][x];
            }
        }
        return rotate;
    }

    static int[][] blur(int[][] image, Editor editor) {
        int height = image.length;
        int width = image[0].length;
        int[][] blur = new int[height][width];
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int sum = 0;
                int count = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        sum += image[y + i][x + j];
                        count++;
                    }
                }
                blur[y][x] = sum / count;
            }
        }
        return blur;
    }

    static int[][] oldEffect(int[][] image, Editor editor) {
        int[][] g = new int[image.length][image[0].length];
        vignette(image, 100);
        for (int y = 0; y < image.length; y++) {
            for (int x = 0; x < image[0].length; x++) {
                g[y][x] = grain(image[y][x], 50);
            }
        }
        margin(g);
        return g;
    }

    static int[][] retro(int[][] image, Editor editor) {
        int[][] a = new int[image.length][image[0].length];
        for (int y = 0; y < image.length; y++) {
            for (int x = 0; x < image[0].length; x++) {
                a[y][x] = contrast(image[y][x], 50);
            }
        }
        vignette(image, 100);

        return blur(a, editor);
    }
    // Livre

    static int[][] invertColors(int[][] image, Editor editor) {
        int height = image.length;
        int width = image[0].length;
        int[][] inverted = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                inverted[y][x] = 255 - image[y][x];
            }
        }
        return inverted;
    }

    static int[][] sharpEdges(int[][] image, Editor editor) {
        int height = image.length;
        int width = image[0].length;
        int[][] edges = new int[height][width];

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int ex = -image[y - 1][x - 1] - 2 * image[y][x - 1] - image[y + 1][x - 1] +
                        image[y - 1][x + 1] + 2 * image[y][x + 1] + image[y + 1][x + 1];

                int ey = -image[y - 1][x - 1] - 2 * image[y - 1][x] - image[y - 1][x + 1] +
                        image[y + 1][x - 1] + 2 * image[y + 1][x] + image[y + 1][x + 1];

                int edgeValue = Math.min(255, Math.max(0, (int) Math.sqrt(ex * ex + ey * ey)));
                edges[y][x] = edgeValue;
            }
        }
        return edges;
    }
}
