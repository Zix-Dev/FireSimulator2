package Model;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PaletteSaver extends File {

    //Attributes


    private final ArrayList<String> paletteNames = new ArrayList<>();
    private final ArrayList<Palette> palettes = new ArrayList<>();


    //Constructors


    public PaletteSaver() {
        super("src\\main\\resources\\palettes.json");
        while (!exists()) {
            try {
                createJSON();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        try {
            importPalettes();
            importPaletteNames();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    //Methods


    private int[] extractColors(String s) {
        ArrayList<String> arg = substringBetweenChars(s,':',',');
        int a = Integer.parseInt(arg.get(0));
        int r = Integer.parseInt(arg.get(1));
        int g = Integer.parseInt(arg.get(2));
        int b = Integer.parseInt(substringBetweenChars(s,':','}').get(0));
        return new int[]{a,r,g,b};
    }

    private ArrayList<String> substringBetweenChars(String s, char beginChar, char endChar) {
        int begin = -1;
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == endChar && begin > 0) {
                strings.add(s.substring(begin+1,i));
                begin = -1;
            } else if (s.charAt(i) == beginChar) {
                begin = i;
            }
        }
        return strings;
    }

    public void add(String name, Palette palette) {
        paletteNames.add(name);
        palettes.add(palette);
        try {
            rewrite();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void createJSON() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("src\\main\\resources\\palettes.json"));
        bw.write("{\n" +
                "  \"palettes\":[\n" +
                "    {\n" +
                "      \"name\":\"Standard\",\n" +
                "      \"baseColors\":[\n" +
                "        {\"a\":255,\"r\":0,\"g\":0,\"b\":0},\n" +
                "        {\"a\":255,\"r\":145,\"g\":80,\"b\":50},\n" +
                "        {\"a\":255,\"r\":210,\"g\":120,\"b\":50},\n" +
                "        {\"a\":255,\"r\":255,\"g\":255,\"b\":120},\n" +
                "        {\"a\":255,\"r\":255,\"g\":255,\"b\":255}\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"Gray\",\n" +
                "      \"baseColors\":[\n" +
                "        {\"a\":255,\"r\":0,\"g\":0,\"b\":0},\n" +
                "        {\"a\":255,\"r\":255,\"g\":255,\"b\":255}\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}");
        bw.close();
    }

    public String createPaletteJson(String name, Palette palette) {
        String s = "    {\n" +
                "      \"name\":\""+name+"\",\n" +
                "      \"baseColors\":[\n";

        Color[] baseColors = palette.getBaseColors();

        for (int i = 0; i < baseColors.length; i++) {

            Color c = baseColors[i];
            int a = c.getAlpha(), r = c.getRed(), g = c.getGreen(), b = c.getBlue();

            if (i == baseColors.length - 1) {
                s = s.concat("        {\"a\":"+a+",\"r\":"+r+",\"g\":"+g+",\"b\":"+b+"}\n" +
                         "      ]\n" +
                         "    }");
            } else {
                s = s.concat("        {\"a\":"+a+",\"r\":"+r+",\"g\":"+g+",\"b\":"+b+"},\n");
            }
        }
        return s;
    }


    public Palette get(String selected) {

        for (int i = 0; i < palettes.size(); i++) {
            if (paletteNames.get(i).equals(selected)) {
                return palettes.get(i);
            }
        }
        return null;
    }

    public ArrayList<String> getPaletteNames() {
        return paletteNames;
    }

    public void importPaletteNames() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(this));
        while (br.ready()) {
            ArrayList<String> strings = substringBetweenChars(br.readLine(),'"','"');
            if (strings.size() > 1 && strings.get(0).equals("name")) {
                paletteNames.add(strings.get(1));
            }
        }
        br.close();
    }

    public void importPalettes() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(this));
        ArrayList<Color> baseColors = new ArrayList<>();
        boolean first = true;

        while (br.ready()) {
            String line = br.readLine();
            ArrayList<String> strings = substringBetweenChars(line,'"','"');

            if (!strings.isEmpty() && strings.get(0).equals("baseColors")) {
                if (!first) {
                    Color[] baseColorsArray = new Color[baseColors.size()];
                    for (int i = 0; i < baseColors.size(); i++) {
                        baseColorsArray[i] = baseColors.get(i);
                    }
                    palettes.add(new Palette(baseColorsArray));
                    baseColors = new ArrayList<>();
                } else {
                    first = false;
                }
            } else if (strings.size() > 3 && strings.get(0).equals("a") && strings.get(1).equals("r") && strings.get(2).equals("g") && strings.get(3).equals("b")) {
                int[] argb = extractColors(line);
                baseColors.add(new Color(argb[1], argb[2], argb[3], argb[0]));
            }
        }
        br.close();
        Color[] baseColorsArray = new Color[baseColors.size()];
        for (int i = 0; i < baseColors.size(); i++) {
            baseColorsArray[i] = baseColors.get(i);
        }
        palettes.add(new Palette(baseColorsArray));
    }

    public void remove(String s) {
        int index = paletteNames.indexOf(s);
        paletteNames.remove(index);
        palettes.remove(index);
        try {
            rewrite();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void rewrite() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(getPath()));
        bw.write("{\n" +
                "  \"palettes\":[\n");
        for (int i = 0; i < palettes.size(); i++) {
            bw.write(createPaletteJson(paletteNames.get(i), palettes.get(i)));
            bw.write((i == palettes.size()-1)?"\n":",\n");
        }
        bw.write("  ]\n" +
                "}");
        bw.close();
    }

}
