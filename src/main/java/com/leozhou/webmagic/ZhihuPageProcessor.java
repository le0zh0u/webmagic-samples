package com.leozhou.webmagic;

import com.leozhou.webmagic.model.ZhihuAnswerItem;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * Created by zhouchunjie on 2016/12/18.
 */
public class ZhihuPageProcessor implements PageProcessor {

    private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.98 Safari/537.36")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4")
            .setCharset("UTF-8");

    private static final int voteNum = 1000;


    public void process(Page page) {
        List<String> relativeUrl = page.getHtml().xpath("//li[@class='item clearfix']/div/a/@href").all();
        page.addTargetRequests(relativeUrl);
        relativeUrl = page.getHtml().xpath("//div[@id='zh-question-related-questions']//a[@class='question_link']/@href").all();
        page.addTargetRequests(relativeUrl);
        String title = page.getHtml().xpath("//div[@id='zh-question-title']/h2[@class='zm-item-title']/span[@class='zm-editable-content']/text()").toString();
        List<String> answers = page.getHtml().xpath("//div[@id='zh-question-answer-wrap']/div").all();
        boolean exist = false;
        int count = 0;
        for (String answer : answers) {
            String vote = new Html(answer).xpath("//div[@class='zm-votebar']//span[@class='count']/text()").toString();
            if (Integer.valueOf(vote) >= voteNum) {
                String code = new Html(answer).xpath("//meta[@itemprop='answer-url-token']/@content").toString();
                ZhihuAnswerItem item = new ZhihuAnswerItem();
                item.setQuestionTitle(title);
                item.setContent(new Html(answer).xpath("//div[@class='zm-editable-content']").toString());
                item.setUserid(new Html(answer).xpath("//a[@class='author-link']/@href").toString());
                item.setVote(vote);
                page.putField(code, item);
                count ++;
                if (count >=2) {
                    exist = true;
                }
            }
        }

        if (!exist) {
            page.setSkip(true);
        }
    }

    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new ZhihuPageProcessor()).addUrl("http://www.zhihu.com/search?type=question&q=java")
                .addPipeline(new JsonFilePipeline("/Users/zhouchunjie/workspace/java-projects/webmagic/webmagic-samples/src/main/resources/export-sample"))
                .thread(5).run();
    }
}
