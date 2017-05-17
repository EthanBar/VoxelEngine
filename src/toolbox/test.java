package toolbox;

import worldgen.OpenSimplexNoise;

import java.util.concurrent.ThreadLocalRandom;

public class test {

    public static void main(String[] args) {
        OpenSimplexNoise noise = new OpenSimplexNoise(ThreadLocalRandom.current().nextLong());
        for (float x = 0; x < 100; x++) {
            for (float y = 0; y < 100; y++) {
                System.out.println(noise.eval(x / 100, y / 100));
            }
        }
    }

}
