package com.example.examsys.Services.implement;

import com.example.examsys.DTO.QuestionDTO;
import com.example.examsys.Entity.Question;
import com.example.examsys.Repository.QuestionRepository;
import com.example.examsys.Services.QuestionsServices;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class QuestionsServicesImplement implements QuestionsServices {
    @Autowired
    private QuestionRepository qr;

    @Cacheable(key = "#p0.getId()", value = "QuestionID#2")
    public Question addQuestion(QuestionDTO questionDTO) {
        try {
            Question question = new Question();
            BeanUtils.copyProperties(questionDTO, question);
            qr.save(question);
            return question;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @CacheEvict(key = "#p0", value = "QuestionID")
    public boolean deleteQuestionById(Long id) {
        try {
            qr.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Cacheable(key = "#p0", value = "QuestionID#2")
    public Question findQuestionById(Long id) {
        try {
            Question question = qr.findById(id).get();
            return question;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Question> findQuestionsById(ArrayList<Long> ids) {
        try {
            ArrayList<Question> questions = new ArrayList<Question>();
            for (Long id : ids) {
                Question t = qr.findById(id).get();
                questions.add(t);
            }
            return questions;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @CachePut(key = "#p0.getId()", value = "QuestionID#2")
    public boolean updateQuestion(QuestionDTO questionDTO) {
        try {
            Question question = new Question();
            BeanUtils.copyProperties(questionDTO, question);
            qr.save(question);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void fillQuestion() {
        Question a = new Question();
        a.setType("简答题");
        a.setId(123456L);
        a.setDescription("光的散射原因");
        qr.save(a);
    }
}