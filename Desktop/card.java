package lhk.ca.card.examples.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lhkwww.indigo.dbutils.OraBlob;

import lhk.ca.card.DaoTools;
import lhk.ca.card.examples.model.Person;
import epm.core.dao.BaseDao;
import epm.core.dao.DaoUtil;
import epm.core.pagelist.Pagination;

public class PersonDao extends BaseDao {

    public List getPersons(Person person) {

        String sql = "SELECT id,first_name,last_name, address "
                + "FROM demo_person";

        return DaoUtil.executeQueryList(DaoTools.getConnName(person), sql,
                new Object[] {}, Person.class);

    }

    public ResultSet getPersonRs(Person person) {
        String sql = "SELECT id,first_name,last_name, address "
                + "FROM demo_person";
        return DaoUtil.executeQuery(DaoTools.getConnName(person), sql,
                new Object[] {});
    }

    // indy标签分页底层处理 数据集合为ResultSet
    public Pagination getPersonPaginationRs(Person person, Pagination pagination)
            throws Exception {

        String sql = "SELECT id,first_name,last_name FROM demo_person";
        String connName = DaoTools.getConnName(person);
        pagination = DaoUtil.queryForPagination(connName, sql, new Object[] {},
                pagination);

        return pagination;
    }

    // cis标签分页底层处理
    public Pagination getPersons(Person person, Pagination pagination)
            throws Exception {

        String connName = DaoTools.getConnName(person);

        String sql = "SELECT id,first_name,last_name FROM demo_person "
                + "where 1= 1 ";

        pagination = DaoUtil.queryForPagination(connName, sql, new Object[] {},
                pagination);

        ResultSet rs = (ResultSet) pagination.getObject();
        List<Person> personList = rs2List(rs);
        pagination.setList(personList);

        return pagination;
    }

    private List<Person> rs2List(ResultSet rs) throws Exception {
        List<Person> personList = new ArrayList<Person>();
        Person person = null;
        while (rs.next()) {
            person = new Person();
            person.setId(rs.getString("id"));
            person.setFirstName(rs.getString("first_name"));
            person.setLastName(rs.getString("last_name"));
            personList.add(person);
        }
        return personList;
    }

    // for test
    public void updatePerson(String firstName, String lastName)
            throws Exception {
        String sql = "UPDATE  demo_person SET last_name = ? WHERE first_name = ?";

        Object[] params = new Object[] { lastName, firstName };
        String connName = DaoTools.getConnName(new Person());
        int a = DaoUtil.executeUpdate(connName, sql, params);
    }

   
    public void saveResume(byte[] value) throws Exception {
        OraBlob.writeBlob("test", "demo_person", "resume", "WHERE id=1", value);
    }

}
