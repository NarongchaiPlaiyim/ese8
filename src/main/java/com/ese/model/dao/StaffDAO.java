package com.ese.model.dao;

import com.ese.model.db.StaffModel;
import com.ese.model.view.report.UserAndRoleViewReport;
import com.ese.utils.Utils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StaffDAO extends GenericDAO<StaffModel, Integer> implements StaffDAOInf{

    @Override
    public StaffModel findByUserNameAndPassword(String userName, String password) throws Exception {
        Criteria criteria = getCriteria().add(Restrictions.eq("username", userName))
                                         .add(Restrictions.eq("password", password))
                                         .add(Restrictions.eq("isValid", 1));
        return findOneByCriteria(criteria);
    }

    @Override
    public boolean isUsernameExist(String userName) throws Exception {
        Criteria criteria = getCriteria().add(Restrictions.eq("username", userName))
                                         .add(Restrictions.eq("username", userName));
        return isRecordExist(criteria);
    }

    public List<StaffModel> test() throws Exception {
//        SELECT * FROM dbo.staff
        return findBySQL("SELECT * FROM dbo.staff", "3", 5, 6);
    }

    @Override
    public List<StaffModel> findUserByIsValid(){
        List<StaffModel> staffModels = Utils.getEmptyList();
        try {
            Criteria criteria = getSession().createCriteria(StaffModel.class, "s")
                                            .add(Restrictions.eq("isValid", 1))
                                            .createAlias("s.factionModel", "f")
                                            .createAlias("f.msDepartmentModel", "d")
                                            .addOrder(Order.asc("d.id"));
            staffModels = findByCriteria(criteria);
        } catch (Exception e) {
            log.debug("Exception error findUserByIsValid : ", e);
        }

        return staffModels;
    }

    @Override
    public List<StaffModel> findUserBySearch(int departmentId, int factionId, String keySearch) {
        log.debug("departmentId : {}, factionId : {}, keySearch : {}", departmentId, factionId, keySearch);
        List<StaffModel> staffModelList = Utils.getEmptyList();
        try {
            Criteria criteria = getSession().createCriteria(StaffModel.class, "s")
                                            .add(Restrictions.eq("isValid", 1))
                                            .createAlias("s.factionModel", "f")
                                            .createAlias("f.msDepartmentModel", "d")
                                            .addOrder(Order.asc("d.id"));

            if (!Utils.isZero(factionId) &&
                !Utils.isZero(departmentId)){
                criteria.add(Restrictions.eq("factionModel.id", factionId));
            }

            if (!Utils.isNull(keySearch.trim()) &&
                keySearch.length() > 0){
                Criterion name = Restrictions.like("name", "%" + keySearch.trim() + "%");
                Criterion userName = Restrictions.like("username", "%"+keySearch.trim()+"%");
                criteria.add(Restrictions.or(name,userName));
            }

            if (!Utils.isZero(departmentId)){
                criteria.add(Restrictions.eq("d.id", departmentId));
            }

            staffModelList = findByCriteria(criteria);
        } catch (Exception e) {
            log.debug("Exception error findUserByIsValid : ", e);
        }
        return staffModelList;
    }

    @Override
    public void deleteByUpdate(StaffModel model) throws Exception{
        model.setIsValid(0);
        model.setUpdateDate(Utils.currentDate());
        update(model);
    }

    @Override
    public StaffModel findByUserName(String userName) throws Exception {
        return findOneByCriteria(Restrictions.eq("username", userName));
    }

    @Override
    public List<UserAndRoleViewReport> genSQLReportUserAndRole(){
        List<UserAndRoleViewReport> userAndRoleViewReportList = new ArrayList();
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append(" SELECT ");
        sqlBuilder.append(" ").append(getPrefix()).append(".department.name AS DEPART_NAME, ");
        sqlBuilder.append(" ").append(getPrefix()).append(".faction.name AS FACTION_NAME, ");
        sqlBuilder.append(" ").append(getPrefix()).append(".title.name AS TITLE, ");
        sqlBuilder.append(" ").append(getPrefix()).append(".staff.name AS NAME, ");
        sqlBuilder.append(" ").append(getPrefix()).append(".staff.username AS LOGIN_NAME, ");
        sqlBuilder.append(" ").append(getPrefix()).append(".staff.position AS POSITION, ");
        sqlBuilder.append(" ").append(getPrefix()).append(".staff.create_date AS CREATE_DATE, ");
        sqlBuilder.append(" ").append(getPrefix()).append(".system_role.code AS ROLE ");
        sqlBuilder.append(" FROM ").append(getPrefix()).append(".staff");
        sqlBuilder.append(" LEFT JOIN ").append(getPrefix()).append(".title");
        sqlBuilder.append(" ON  ").append(getPrefix()).append(".staff.title_id = ").append(getPrefix()).append(".title.id");
        sqlBuilder.append(" LEFT JOIN ").append(getPrefix()).append(".staff_roles");
        sqlBuilder.append(" ON ").append(getPrefix()).append(".staff.id = ").append(getPrefix()).append(".staff_roles.staff");
        sqlBuilder.append(" LEFT JOIN ").append(getPrefix()).append(".system_role");
        sqlBuilder.append(" ON ").append(getPrefix()).append(".staff_roles.roles = ").append(getPrefix()).append(".system_role.id");
        sqlBuilder.append(" LEFT JOIN ").append(getPrefix()).append(".faction");
        sqlBuilder.append(" ON ").append(getPrefix()).append(".staff.faction_id = ").append(getPrefix()).append(".faction.id");
        sqlBuilder.append(" LEFT JOIN ").append(getPrefix()).append(".department");
        sqlBuilder.append(" ON ").append(getPrefix()).append(".faction.department_id = ").append(getPrefix()).append(".department.id");
        sqlBuilder.append(" GROUP BY ").append(getPrefix()).append(".department.name,");
        sqlBuilder.append(" ").append(getPrefix()).append(".faction.name,").append(getPrefix()).append(".title.name,");
        sqlBuilder.append(" ").append(getPrefix()).append(".staff.name,").append(getPrefix()).append(".staff.username,").append(getPrefix()).append(".staff.position,");
        sqlBuilder.append(" ").append(getPrefix()).append(".staff.create_date,").append(getPrefix()).append(".system_role.code");

        log.debug("--SQL {}",sqlBuilder.toString());

        try {
            SQLQuery query = getSession().createSQLQuery(sqlBuilder.toString())
                                         .addScalar("DEPART_NAME", StringType.INSTANCE)
                                         .addScalar("FACTION_NAME", StringType.INSTANCE)
                                         .addScalar("TITLE", StringType.INSTANCE)
                                         .addScalar("NAME", StringType.INSTANCE)
                                         .addScalar("LOGIN_NAME", StringType.INSTANCE)
                                         .addScalar("POSITION", StringType.INSTANCE)
                                         .addScalar("CREATE_DATE", TimestampType.INSTANCE)
                                         .addScalar("ROLE", StringType.INSTANCE);

            List<Object[]> objects = query.list();


            objects.stream().forEach(entity -> {
                        UserAndRoleViewReport report = UserAndRoleViewReport.builder()
                                                                            .department(Utils.parseString(entity[0], ""))
                                                                            .faction(Utils.parseString(entity[1], ""))
                                                                            .title(Utils.parseString(entity[2], ""))
                                                                            .name(Utils.parseString(entity[3], ""))
                                                                            .loginName(Utils.parseString(entity[4], ""))
                                                                            .position(Utils.parseString(entity[5], ""))
                                                                            .createDate(Utils.convertDateToString(Utils.parseDate(entity[6], null)))
                                                                            .role(Utils.parseString(entity[7], ""));
                        userAndRoleViewReportList.add(report);
                    }
            );
        } catch (Exception e) {
            log.debug("Exception SQL : {}", e);
        }

        return userAndRoleViewReportList;
    }

}
