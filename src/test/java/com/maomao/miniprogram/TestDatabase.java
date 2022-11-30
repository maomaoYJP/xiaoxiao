package com.maomao.miniprogram;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maomao.miniprogram.model.dto.CardQueryRequest;
import com.maomao.miniprogram.model.dto.TalkCommentQueryRequest;
import com.maomao.miniprogram.model.dto.TalkSaveDTO;
import com.maomao.miniprogram.model.vo.CommentVO;
import com.maomao.miniprogram.model.vo.IndexCardVO;
import com.maomao.miniprogram.model.vo.TalkVO;
import com.maomao.miniprogram.service.CommentService;
import com.maomao.miniprogram.service.TalkService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author maomao
 * 2022/10/29 21:41
 */
@SpringBootTest
public class TestDatabase {

    @Resource
    TalkService talkService;

    @Resource
    CommentService commentService;

    @Test
    void testUser(){
        CardQueryRequest cardQueryRequest = new CardQueryRequest();
        cardQueryRequest.setPageNum(2);
        cardQueryRequest.setPageSize(1);
        Page<IndexCardVO> talkCard = talkService.getTalkCard(cardQueryRequest,1L);
        System.out.println(talkCard.getRecords());
    }

    @Test
    void testThumb(){
        Integer integer = talkService.talkThumb(1L,1L);
        System.out.println(integer);
    }

    @Test
    void testTalk(){
        TalkVO talk = talkService.getTalk(1L);
        System.out.println(talk);
    }

    @Test
    void testComment(){
        TalkCommentQueryRequest talkCommentQueryRequest = new TalkCommentQueryRequest();
        talkCommentQueryRequest.setTalkId(1L);

    }

    @Test
    void testTalkSave(){
        TalkSaveDTO talkSaveDTO = new TalkSaveDTO();
        talkSaveDTO.setTitle("测试文章");
        talkSaveDTO.setContent("我是测试我是测试");
        String[] urls = new String[]{"https://gdufe-campus.oss-cn-guangzhou.aliyuncs.com/2022-11-25/16693056724309547f3e7-55bc-464e-8cc6-34cda50f19ad",
        "https://gdufe-campus.oss-cn-guangzhou.aliyuncs.com/2022-11-25/1669305761127dfd6eff7-693b-4af3-9ddc-93af0a6f83a8"};
        talkSaveDTO.setUrls(urls);
        System.out.println(talkService.saveTalkAndPicture(talkSaveDTO));
    }
}
