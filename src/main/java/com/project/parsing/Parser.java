package com.project.parsing;

import com.project.parsing.model.Unit;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    String WebURL;
    public void init() throws Exception{
        //base url = https://www.work.ua/jobs-it/
        Document global = Jsoup.connect(WebURL).get();

    }
    @SneakyThrows
    public Unit ParseUnit(String URL){
        Document global = Jsoup.connect(URL).get();
        String jobName = global.getElementById("h1-name").text();
        String aboutJob = global.getElementById("job-description").text();
        String jobSalaryRange = global.select("#center > div.design-section.design-wrapper.block-relative > div > div > div.col-md-8 > div > div.card.rounded-0-top.rounded-20-top-xs.pb-lg > div.wordwrap.mt-lg.sm\\:mt-0 > ul > li:nth-child(1) > span.strong-500").text();

        Unit unit = new Unit(jobName, aboutJob, jobSalaryRange);
        return unit;
    }
    @SneakyThrows
    public List<Unit> ParseURLs(String baseURL){
        String mainPageURL = "https://www.work.ua";
        String finalLink;
        Document global = Jsoup.connect(baseURL).get();
        List<String> urls = global.getElementById("pjax-jobs-list").stream()
                .map(element -> element.select("a").attr("href"))
                .filter(href->href!=null && !href.isBlank() && href.contains("/jobs/")&& !href.contains("by-company"))
                .distinct()
                .toList();
        List<Unit> result = new ArrayList<>();
        if(urls.isEmpty()){
            System.out.println("Empty list after parsing given URL");
            result.add(new Unit("0","0","0"));
            return result;
        }else{
            for(String links: urls){
                finalLink = mainPageURL+links;
                result.add(ParseUnit(finalLink));
            }
        }
        return result;
        //#pjax-jobs-list
    }

    //base url = https://www.work.ua/jobs-it/ ... <- job link
}
