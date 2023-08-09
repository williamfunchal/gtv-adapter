package com.consensus.gtvadapter.module.poller.service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class CsvParserService<T> {

    public List<T> parseCsvFile(String filePath, Class<T> type) throws IOException {
        Path path = Path.of(filePath);
        try (Reader reader = Files.newBufferedReader(path)) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        }
    }
}