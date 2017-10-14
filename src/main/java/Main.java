import org.apache.commons.lang3.text.StrTokenizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by yzhao on 10/13/17.
 */
public class Main {
    public static void main(String[] args) {
        String currentTime = getCurrentDate();
        // Location of file to read
        File file = new File("/Users/yzhao/Desktop/cookie_map.csv");
        File fout = new File("/Users/yzhao/Desktop/" + currentTime + "-000001.united-lax1." + currentTime + "000" + ".csv"); // 文件名不能有.force，否则不会有clog文件，则不会load到netezza
        Scanner scanner = null;
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        int count = 0;
        try {
            scanner = new Scanner(file);
            fos = new FileOutputStream(fout);
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                String[] result = csvFormatSplitCharDelimiter('|', line);
                // ckvraw|20|103467387633636100|2044=;12345|103467387633636100|80|80|80
                if(result!= null && result.length==4) {
                    try {
                        result[0] = String.valueOf(dateToUnixTime(result[0]));
                        bw.write("cm" + "|" + result[0] + "|" + result[1] + "|" + result[2] + "=" + result[3]);
                        bw.newLine();
                        count++;
                    }catch (Exception e){

                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Count row:" + count);
    }

    private static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return dateFormat.format(date).toString();
    }

    public static String[] csvFormatSplitCharDelimiter(char delimiterChar, String target){
        StrTokenizer st = StrTokenizer.getCSVInstance();
        st.setDelimiterChar(delimiterChar);
        String[] result = st.reset(target).getTokenArray();
        return result;
    }

    public static Long dateToUnixTime(String dateString ) throws Exception{
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = dateFormat.parse(dateString );
        long unixTime = date.getTime()/1000;
        return unixTime;
    }
}
