package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.example.dto.ApplyDto;
import com.example.dto.HomeDto;
import com.example.dto.ResultDto;
import com.example.exception.MyException;
import com.example.exception.TestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

/**
 * 使用RestController就会对所有的Mapping方法返回值都加上@ResponseBody把结果交给MessageConverter处理。
 * @ResponseBody的作用就是把结果交给MessageConverter转化成输出给到客户端，而不走模型视图渲染的流程。
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Value("${app.name}")
    private String appName;

    @Value("#{systemProperties['os.name']}")
    private String osName;

    /**
     * http://localhost:8080/home/greet
     * {"content":"welcome home"}
     * 这里前段之所以能看到JSON的返回格式，是因为采用的MessageConverter是MappingJackson2HttpMessageConverter,
     * 需要加入Jackson的依赖(jackson-core, jackson-databind, jackson-annotations)，不然会报错。
     */
    @GetMapping("/greet")
    public HomeDto greet(){
        log.info("app.name:{}, app.version:{}", env.getProperty("app.name"), env.getProperty("app.version"));

        return new HomeDto("welcome to: " + appName + " ,os: " + osName);
    }

    /**
     * http://localhost:8080/home/echo?content=hello-world
     * hello-world
     */
    @GetMapping("/echo")
    public String echo(@RequestParam("content") String content){
        return content;
    }

    /**
     * http://localhost:8080/home/echo2/hello-nihao
     * hello-nihao
     */
    @GetMapping("/echo2/{content}")
    public String echo2(@PathVariable("content") String content){
        return content;
    }

    /**
     * curl -d '{"name":"Eric","age":28}' -H 'Content-Type: application/json' http://localhost:8080/home/apply
     *
     * HTTP/1.1 201
     * Content-Type: text/plain;charset=ISO-8859-1
     * Content-Length: 2
     * Date: Tue, 10 Sep 2019 14:44:20 GMT
     *
     * ok
     */
    @PostMapping("/apply")
    @ResponseStatus(HttpStatus.CREATED) //自定义HTTP返回码
    public String apply(@RequestBody ApplyDto applyDto){
        log.info("apply info:{}", JSON.toJSONString(applyDto));
        return "ok";
    }

    /**
     * curl -i -d '{"name":"Eric","age":28}' -H 'Content-Type: application/json' http://localhost:8080/home/apply2
     *
     * HTTP/1.1 201
     * myInfo: test header info
     * Content-Type: application/json;charset=UTF-8
     * Transfer-Encoding: chunked
     * Date: Tue, 10 Sep 2019 14:42:15 GMT
     *
     * {"msg":"done"}
     */
    @PostMapping("/apply2")
    public ResponseEntity<ResultDto> apply2(@RequestBody ApplyDto applyDto){
        log.info("apply info:{}", JSON.toJSONString(applyDto));
        ResultDto resultDto = new ResultDto();
        resultDto.setMsg("done");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set("myInfo", "test header info");

        return new ResponseEntity<ResultDto>(resultDto, map, HttpStatus.CREATED);
    }

    /**
     * http://localhost:8080/home/find/9999
     * info not found
     */
    @GetMapping("/find/{id}")
    public HomeDto find(@PathVariable("id") Long id){
        if(9999L == id){
            throw new MyException();//这里抛出了异常
        }
        return new HomeDto("hello ni hao");
    }

    @GetMapping("/find2/{id}")
    public HomeDto find2(@PathVariable("id") Long id){
        if(9999L == id){
            throw new TestException();//这里抛出了异常
        }
        return new HomeDto("hello ni hao");
    }

    /**
     * 捕获该Controller抛出的MyException异常,处理后返回给客户端信息，
     * 客户端会看到 info not found 的信息, 这里如果返回的是200的码
     * 如果返回的是406-NOT_FOUND,那么看不到info not found的信息的
     *
     * 如果想要对所有的Controller的异常捕获，应该使用@ControllerAdvice定义
     * 一个类（标注了ControllerAdvice也算bean），在里面统一处理异常
     */
    @ExceptionHandler(MyException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "resource not found")
    public String handleMyException(MyException e){
        log.warn("exception info:", e);
        return "info not found";
    }
}
