package com.example.demo.image;

import com.example.demo.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@PropertySource("classpath:/application-file.properties")
public class ImageApiController {
    @Autowired
    ImageService imageService;
    @Autowired
    BoardService boardService;
    @Value("${file.path}")
    private String savePath;



}
