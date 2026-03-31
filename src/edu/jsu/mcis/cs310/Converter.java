package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class Converter {

    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        String result = "{}";
        try {
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> records = reader.readAll();
            String[] headers = records.get(0);
            JsonArray colHeadings = new JsonArray();
            JsonArray prodNums = new JsonArray();
            JsonArray data = new JsonArray();
            for (String header : headers) {
                colHeadings.add(header);
            }
            for (int i = 1; i < records.size(); i++) {
                String[] row = records.get(i);
                prodNums.add(row[0]);
                JsonArray rowData = new JsonArray();
                for (int j = 1; j < row.length; j++) {
                    if (j == 2) {
                        rowData.add(Integer.parseInt(row[j]));
                    } else if (j == 3) {
                        rowData.add(Integer.parseInt(row[j]));
                    } else {
                        rowData.add(row[j]);
                    }
                }
                data.add(rowData);
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.put("ProdNums", prodNums);
            jsonObject.put("ColHeadings", colHeadings);
            jsonObject.put("Data", data);
            result = Jsoner.serialize(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }

    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        String result = "";
        try {
            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(jsonString);
            JsonArray colHeadings = (JsonArray) jsonObject.get("ColHeadings");
            JsonArray prodNums = (JsonArray) jsonObject.get("ProdNums");
            JsonArray data = (JsonArray) jsonObject.get("Data");
            StringWriter sw = new StringWriter();
            CSVWriter writer = new CSVWriter(sw);
            String[] headerRow = new String[colHeadings.size()];
            for (int i = 0; i < colHeadings.size(); i++) {
                headerRow[i] = colHeadings.get(i).toString();
            }
            writer.writeNext(headerRow);
            for (int i = 0; i < data.size(); i++) {
                JsonArray rowData = (JsonArray) data.get(i);
                String[] row = new String[rowData.size() + 1];
                row[0] = prodNums.get(i).toString();
                for (int j = 0; j < rowData.size(); j++) {
                    if (j == 2) {
                        int episodeNum = Integer.parseInt(rowData.get(j).toString());
                        row[j + 1] = String.format("%02d", episodeNum);
                    } else {
                        row[j + 1] = rowData.get(j).toString();
                    }
                }
                writer.writeNext(row);
            }
            writer.close();
            result = sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }
}