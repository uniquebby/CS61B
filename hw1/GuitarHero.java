/** A client that uses the synthesizer package to replicate a plucked guitar string sound */
import es.datastructur.synthesizer.GuitarString;

public class GuitarHero {
    private static final double CONCERT[] = {
            /* This keyboard arrangement imitates a piano keyboard:
               The “white keys” are on the qwerty and zxcv rows and the “black keys”
               on the 12345 and asdf rows of the keyboard. */
            440.0 * Math.pow(2, (0.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (1.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (2.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (3.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (4.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (5.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (6.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (7.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (8.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (9.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (10.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (11.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (12.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (13.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (14.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (15.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (16.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (17.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (18.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (19.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (20.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (21.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (22.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (23.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (24.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (25.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (26.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (27.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (28.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (29.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (30.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (31.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (32.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (33.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (34.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (35.0 - 24.0) / 12.0),
            440.0 * Math.pow(2, (36.0 - 24.0) / 12.0)
    };

    public static void main(String[] args) {
        /* create 37 guitar strings, for concert a piano keyboard. */
        GuitarString[] string = {
                new GuitarString(CONCERT[0]),
                new GuitarString(CONCERT[1]),
                new GuitarString(CONCERT[2]),
                new GuitarString(CONCERT[3]),
                new GuitarString(CONCERT[4]),
                new GuitarString(CONCERT[5]),
                new GuitarString(CONCERT[6]),
                new GuitarString(CONCERT[7]),
                new GuitarString(CONCERT[8]),
                new GuitarString(CONCERT[9]),
                new GuitarString(CONCERT[10]),
                new GuitarString(CONCERT[11]),
                new GuitarString(CONCERT[12]),
                new GuitarString(CONCERT[13]),
                new GuitarString(CONCERT[14]),
                new GuitarString(CONCERT[15]),
                new GuitarString(CONCERT[16]),
                new GuitarString(CONCERT[17]),
                new GuitarString(CONCERT[18]),
                new GuitarString(CONCERT[19]),
                new GuitarString(CONCERT[20]),
                new GuitarString(CONCERT[21]),
                new GuitarString(CONCERT[22]),
                new GuitarString(CONCERT[23]),
                new GuitarString(CONCERT[24]),
                new GuitarString(CONCERT[25]),
                new GuitarString(CONCERT[26]),
                new GuitarString(CONCERT[27]),
                new GuitarString(CONCERT[28]),
                new GuitarString(CONCERT[29]),
                new GuitarString(CONCERT[30]),
                new GuitarString(CONCERT[31]),
                new GuitarString(CONCERT[32]),
                new GuitarString(CONCERT[33]),
                new GuitarString(CONCERT[34]),
                new GuitarString(CONCERT[35]),
                new GuitarString(CONCERT[36])
        };

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();

                switch (key) {
                    case 'q':
                        string[0].pluck();
                        break;
                    case '2':
                        string[1].pluck();
                        break;
                    case 'w':
                        string[2].pluck();
                        break;
                    case 'e':
                        string[3].pluck();
                        break;
                    case '4':
                        string[4].pluck();
                        break;
                    case 'r':
                        string[5].pluck();
                        break;
                    case '5':
                        string[6].pluck();
                        break;
                    case 't':
                        string[7].pluck();
                        break;
                    case 'y':
                        string[8].pluck();
                        break;
                    case '7':
                        string[9].pluck();
                        break;
                    case 'u':
                        string[10].pluck();
                        break;
                    case '8':
                        string[11].pluck();
                        break;
                    case 'i':
                        string[12].pluck();
                        break;
                    case '9':
                        string[13].pluck();
                        break;
                    case 'o':
                        string[14].pluck();
                        break;
                    case 'p':
                        string[15].pluck();
                        break;
                    case '-':
                        string[16].pluck();
                        break;
                    case '[':
                        string[17].pluck();
                        break;
                    case '=':
                        string[18].pluck();
                        break;
                    case 'z':
                        string[19].pluck();
                        break;
                    case 'x':
                        string[20].pluck();
                        break;
                    case 'd':
                        string[21].pluck();
                        break;
                    case 'c':
                        string[22].pluck();
                        break;
                    case 'f':
                        string[23].pluck();
                        break;
                    case 'v':
                        string[24].pluck();
                        break;
                    case 'g':
                        string[25].pluck();
                        break;
                    case 'b':
                        string[26].pluck();
                        break;
                    case 'n':
                        string[27].pluck();
                        break;
                    case 'j':
                        string[28].pluck();
                        break;
                    case 'm':
                        string[29].pluck();
                        break;
                    case 'k':
                        string[30].pluck();
                        break;
                    case ',':
                        string[31].pluck();
                        break;
                    case '.':
                        string[32].pluck();
                        break;
                    case ';':
                        string[33].pluck();
                        break;
                    case '/':
                        string[34].pluck();
                        break;
                    case '\'':
                        string[35].pluck();
                        break;
                    case ' ':
                        string[36].pluck();
                        break;
                    default:
                         break;
                }
            }

            /* compute the superposition of samples */
            double sample = 0.0;
            for (int i = 0; i < string.length; ++i) {
                sample += string[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < string.length; ++i) {
                string[i].tic();
            }
        }
    }
}
