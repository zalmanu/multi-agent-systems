package ro.uvt.mas;

import java.util.Arrays;
import java.util.Random;

public enum Topic {
    DTIME, P, EXPTIME, NTIME, NP, NEXPTIME, DSPACE, L, PSPACE, EXPSPACE, NSPACE, NL, NPSPACE, NEXPSPACE;

    private static final Random random = new Random();

    public static Topic getRandomTopic() {
        return Arrays.asList(values()).get(random.nextInt(values().length));
    }
}
