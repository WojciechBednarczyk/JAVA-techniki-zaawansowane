package CSVReader;

import ex.api.DataSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Reader {

    public static DataSet readData() {

        int columns = 0;
        int rows = 0;
        DataSet dataSet = new DataSet();
        Scanner sc = null;
        try {
            sc = new Scanner(new File("client\\src\\main\\resources\\dane.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //read number of columns and rows
        String[] headers = null;
        boolean headersReaded = false;

        while (sc.hasNextLine())
        {
            String line = sc.nextLine();
            String[] array = line.split(";");
            if (headersReaded==false)
            {
                headers=array;
                headersReaded=true;
            }
            else
            {
                rows++;
            }
        }
        columns=headers.length;

        dataSet.setHeader(headers);

        String data[][] = new String[rows][columns];

        try {
            sc = new Scanner(new File("client\\src\\main\\resources\\dane.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sc.nextLine();

        int rowsCount = 0;
        int columnCount = 0;
        while (sc.hasNextLine())
        {
            columnCount = 0;
            String[] line = sc.nextLine().split(";");
            for (String string : line)
            {
                data[rowsCount][columnCount] = string;
                columnCount++;
            }
            rowsCount++;
        }

        dataSet.setData(data);

//test wypisania
//
//        for (int i = 0;i<data.length;i++) {
//            for (int j = 0; j < data[i].length; j++) {
//                System.out.print(data[i][j] + " ");
//            }
//            System.out.println();
//        }

        return dataSet;
    }
}
