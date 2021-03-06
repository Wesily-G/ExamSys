package com.example.examsys.Services.implement;

import com.example.examsys.DTO.TestDTO;
import com.example.examsys.Entity.Question;
import com.example.examsys.Entity.Test;
import com.example.examsys.Repository.QuestionRepository;
import com.example.examsys.Repository.TestRepository;
import com.example.examsys.Services.TestServices;
import com.example.examsys.Support.MyTool;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class TestServicesImplement implements TestServices {
    @Autowired
    private TestRepository tr;
    @Autowired
    private QuestionRepository qr;

    @Cacheable(key = "#p0.getId()", value = "TestID#5")
    public Test addTest(TestDTO testDTO) throws ParseException {
        Test test = new Test();
        BeanUtils.copyProperties(testDTO, test);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        test.setTimeBegin(simpleDateFormat.parse(testDTO.getTimeBegin()));
        test.setTimeFinish(simpleDateFormat.parse(testDTO.getTimeFinish()));
        tr.save(test);
        return test;
    }

    @Override
    public Test addTestAutomatic(TestDTO testDTO, String subject, HashMap<String, Integer> typeNumberMap) throws ParseException {
        //首先找到所有的相关学科和类型的问题
        HashMap<String, ArrayList<Question>> relatedQuestions = new HashMap<>();
        //组成的问卷
        ArrayList<Question> questions = new ArrayList<>();
        ArrayList<Long> questionsID = new ArrayList<>();
        for (String type : typeNumberMap.keySet()) {
            relatedQuestions.put(type, qr.findAllBySubjectAndType(subject, type));
        }
        //然后根据问题个数随机取出组成问卷
        for (String type : typeNumberMap.keySet()) {
            int number = typeNumberMap.get(type);
            questions.addAll(MyTool.randomGetQuestions(relatedQuestions.get(type), number));
        }
        //然后取出id交给test,上传到数据库
        for (Question question : questions) {
            questionsID.add(question.getId());
        }
        Test test = new Test();
        BeanUtils.copyProperties(testDTO, test);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        test.setTimeBegin(simpleDateFormat.parse(testDTO.getTimeBegin()));
        test.setTimeFinish(simpleDateFormat.parse(testDTO.getTimeFinish()));
        test.setQuestionsID(questionsID);
        tr.save(test);
        return test;
    }

    @CacheEvict(key = "#p0", value = "TestID")
    public boolean deleteTestById(Long id) {
        tr.deleteById(id);
        return true;
    }

    @Cacheable(key = "#p0", value = "TestID#5")
    public Test findTestById(Long id) {
        Test test = tr.findById(id).get();
        return test;
    }

    @CachePut(key = "#p0.getId()", value = "TestID#5")
    public boolean updateTest(TestDTO testDTO) throws ParseException {
        Test test = new Test();
        BeanUtils.copyProperties(testDTO, test);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        test.setTimeBegin(simpleDateFormat.parse(testDTO.getTimeBegin()));
        test.setTimeFinish(simpleDateFormat.parse(testDTO.getTimeFinish()));
        tr.save(test);
        return true;
    }

    @Override
    public ArrayList<Test> findAllTests() {
        ArrayList<Test> tests = new ArrayList<>(tr.findAll());
        return tests;
    }

    public void fillTest() {
        Test a = new Test();
        a.setId(123456L);
        tr.save(a);
    }
}