package com.github.lawlie8;


import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Stream;

public class FileEval {

    public Long FileCount(String pathToFile) throws Exception{
        Stream<String> fileStream = Files.lines(Paths.get(pathToFile));
        return fileStream.count();
    }

    public TreeMap<String,Integer> fetchLineHashes(String pathToFile) throws Exception{
        TreeMap<String,Integer> hashTable = new TreeMap<>();

        FileInputStream fstream = new FileInputStream(pathToFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        int i = 0;
        String line;
        while ((line = br.readLine()) != null){
            String md5Hex;
            if(line.isEmpty()){
                 md5Hex = DigestUtils.md5Hex("").toLowerCase();
            }else {
                md5Hex = DigestUtils.md5Hex(line).toLowerCase();
            }
            hashTable.put(md5Hex,i);
            i++;
        }
        br.close();
        return hashTable;
    }

    public String computeMd5FileHash(String pathToFile) throws Exception{
        byte[] fileStream = Files.readAllBytes(Paths.get(pathToFile));
        return DigestUtils.md5Hex(fileStream).toLowerCase();
    }
    
    public void appendMissingDelimeterLines (int count,String pathToFile) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile,true));
        for (int i = 0; i < count; i++) {
            writer.write(System.lineSeparator());
        }
        writer.close();
    }

    public void fillFileLines(String pathToSource,String pathToTarget) throws Exception{
        Long srcLineCount = FileCount(pathToSource);
        Long trgtLineCount = FileCount(pathToTarget);
        Long emptyLineFillCount = Math.abs(srcLineCount - trgtLineCount);
        if(srcLineCount > trgtLineCount){
            appendMissingDelimeterLines(Math.toIntExact(emptyLineFillCount),pathToTarget);
        }else {
            appendMissingDelimeterLines(Math.toIntExact(emptyLineFillCount),pathToSource);
        }
    }


}
