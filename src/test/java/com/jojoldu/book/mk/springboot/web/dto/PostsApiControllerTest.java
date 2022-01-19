package com.jojoldu.book.mk.springboot.web.dto;

import com.jojoldu.book.mk.springboot.domain.posts.Posts;
import com.jojoldu.book.mk.springboot.domain.posts.PostsRepository;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate resetTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_등록된다() throws Exception{

        String title = "게시글 제목";
        String content = "게시글 본문";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("someone author")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

//        when
        ResponseEntity<Long> responseEntity = resetTemplate.postForEntity(url, requestDto, Long.class);

//        then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

    }

    @Test
    public void Posts_수정된다() throws Exception{
//        given

        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());
//        DB에 test row 추가 (given id | title | content | author)
        
        Long updateId = savedPosts.getId(); // 해당 ID 찾기
        String expectedTitle = "title2";
        String expectedContent = "content2";

//        DTO 생성(변경 트랜잭션을 위해)
        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

//        POST URL
        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

//        Http Entity 정의 (reqeustDTO 를 정의. 해당 Object는 Header, Body를 가지고 있음.)
//        Body 내에 title, content 포함 중
        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

//        when
        ResponseEntity<Long> responseEntity = resetTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

//        then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);

    }

}
