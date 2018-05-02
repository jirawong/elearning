/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.linksinnovation.elearning.repository;

import com.linksinnovation.elearning.dto.QuizConditionDTO;
import com.linksinnovation.elearning.model.report.QuizReport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jirawong Wongdokpuang <jirawong@linksinnovation.com>
 */
@Repository
public class QuizReportRepository {

    @PersistenceContext
    private EntityManager em;

    private static final String query = "select course,name,pass,total,update_date from report_quiz";

    public List<QuizReport> findQuizReport(QuizConditionDTO conditionDTO) {
        if (null != conditionDTO.getCategory()) {
            String queryString = query + " WHERE category_id=:cat";

            if (null != conditionDTO.getSubcategory()) {
                queryString = queryString + " AND sub_category_id=:subcat";
            }
            if (null != conditionDTO.getCourse()) {
                queryString = queryString + " AND course_id=:course";
            }
            if (!conditionDTO.getName().isEmpty()) {
                queryString = queryString + " AND name LIKE :name";
            }
            if (conditionDTO.getStart() != null && conditionDTO.getEnd() != null) {
                queryString = queryString + " AND update_date BETWEEN :start AND :end";
            }

            queryString = queryString + " ORDER BY course_id,name ASC";
            Query q = em.createNativeQuery(queryString);
            q.setParameter("cat", conditionDTO.getCategory());

            if (null != conditionDTO.getSubcategory()) {
                q.setParameter("subcat", conditionDTO.getSubcategory());
            }
            if (null != conditionDTO.getCourse()) {
                q.setParameter("course", conditionDTO.getCourse());
            }
            if (!conditionDTO.getName().isEmpty()) {
                q.setParameter("name", "%"+conditionDTO.getName()+"%");
            }
            if (conditionDTO.getStart() != null && conditionDTO.getEnd() != null) {
                q.setParameter("start", conditionDTO.getStart());
                q.setParameter("end", conditionDTO.getEnd());
            }

            return mapObject(q.getResultList());
        } else if (null == conditionDTO.getCategory()) {
            String queryString = query;

            if (!conditionDTO.getName().isEmpty()) {
                queryString = queryString + " WHERE name LIKE :name";
                if (conditionDTO.getStart() != null && conditionDTO.getEnd() != null) {
                    queryString = queryString + " AND update_date BETWEEN :start AND :end";
                }
            }else{
                if (conditionDTO.getStart() != null && conditionDTO.getEnd() != null) {
                    queryString = queryString + " WHERE update_date BETWEEN :start AND :end";
                }
            }

            queryString = queryString + " ORDER BY course_id,name ASC";
            Query q = em.createNativeQuery(queryString);

            if (!conditionDTO.getName().isEmpty()) {
                q.setParameter("name", "%"+conditionDTO.getName()+"%");
            }
            if (conditionDTO.getStart() != null && conditionDTO.getEnd() != null) {
                q.setParameter("start", conditionDTO.getStart());
                q.setParameter("end", conditionDTO.getEnd());
            }

            return mapObject(q.getResultList());
        } else {
            String queryString = query + " ORDER BY course_id,name ASC";
            Query q = em.createNativeQuery(queryString);
            return mapObject(q.getResultList());
        }
    }

    private List<QuizReport> mapObject(List<Object[]> resultList) {
        List<QuizReport> quizReports = new ArrayList<>();
        resultList.stream().forEach((o) -> {
            quizReports.add(new QuizReport("" + o[0], "" + o[1], (Integer) o[2], (Integer) o[3], (Date) o[4]));
        });
        return quizReports;
    }
}
