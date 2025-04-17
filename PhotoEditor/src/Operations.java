import pt.iscte.greyditor.Editor;
import pt.iscte.greyditor.Selection;

public class Operations {
    static int[][] memory = null;
    static boolean cutOperation = false;
    static int[][] lastImage = null;

    static int[][] copy(int[][] image, Editor editor) {
        Selection selection = editor.getSelection();
        if (selection == null)
            return null;
        int h = selection.height();
        int w = selection.width();
        memory = new int[h][w];

        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                memory[y][x] = image[selection.y() + y][selection.x() + x];
        cutOperation = false;
        return null;
    }

    static int[][] cut(int[][] image, Editor editor) {
        Selection selection = editor.getSelection();
        if (selection == null)
            return null;
        saveState(image);
        int h = selection.height();
        int w = selection.width();
        memory = new int[h][w];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                memory[y][x] = image[selection.y() + y][selection.x() + x];
                image[selection.y() + y][selection.x() + x] = 255;
            }
        }
        cutOperation = true;
        return image;
    }

    static int[][] paste(int[][] image, Editor editor) {
        Selection selection = editor.getSelection();
        if (memory == null)
            return null;
        if (selection == null)
            return null;
        saveState(image);
        int h = memory.length;
        int w = memory[0].length;

        for (int y = 0; y < h && selection.y() + y < image.length; y++) {
            for (int x = 0; x < w && selection.x() + x < image[0].length; x++) {
                image[selection.y() + y][selection.x() + x] = memory[y][x];
            }
        }
        if (cutOperation) {
            memory = null;
        }
        return image;
    }

    static int[][] undo(int[][] image, Editor editor) {
        if (lastImage == null)
            return image;
        for (int y = 0; y < image.length; y++) {
            for (int x = 0; x < image[0].length; x++) {
                image[y][x] = lastImage[y][x];
            }
        }
        lastImage = null;
        return image;
    }

    private static void saveState(int[][] image) {
        lastImage = new int[image.length][image[0].length];
        for (int y = 0; y < image.length; y++) {
            for (int x = 0; x < image[0].length; x++) {
                lastImage[y][x] = image[y][x];
            }
        }
    }
}
