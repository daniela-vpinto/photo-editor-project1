import pt.iscte.greyditor.Editor;

public class MyEditor {
    int y = 0;

    int[][] lineHorizontal(int[][] image, Editor editor) {
        for (int x = 0; x < image[0].length; x++)
            image[y][x] = 0;

        y += 20;
        return null;
    }

    int x = 0;

    int[][] lineVertical(int[][] image, Editor editor) {
        for (int y = 0; y < image.length; y++)
            image[y][x] = 0;

        x += 20;
        return null;
    }


    int[][] slice(int[][] image, Editor editor) {
        int[][] slice = new int[image.length - 10][image[0].length];

        for (int y = 0; y < slice.length; y++) {
            for (int x = 0; x < slice[y].length; x++) {
                slice[y][x] = image[y][x];

            }
        }
        y += 15;
        return slice;
    }

    int[][] randomLine(int[][] image, Editor editor) {
        double r = Math.random();
        if(r < 0.5){
            return lineVertical(image, editor);
        } else {
            return lineHorizontal(image, editor);
        }
    }
    int n = 0;
    int[][] alternateLines(int[][] image, Editor editor) {
        n++;
        if(n%2 == 0){
            return lineVertical(image, editor);
        }
        else {
            return lineHorizontal(image, editor);
        }

    }
}
