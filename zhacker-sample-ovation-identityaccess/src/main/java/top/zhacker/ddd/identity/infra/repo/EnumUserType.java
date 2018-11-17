//   Copyright 2012,2013 Vaughn Vernon
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

package top.zhacker.ddd.identity.infra.repo;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;


public class EnumUserType<E extends Enum<E>> implements UserType {

    private static final int[] SQL_TYPES = { Types.VARCHAR };

    private Class<E> clazz = null;

    protected EnumUserType(Class<E> c) {
        this.clazz = c;
    }

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public Class<?> returnedClass() {
        return clazz;
    }

//    public Object nullSafeGet(
//            ResultSet resultSet,
//            String[] names,
//            Object owner)
//    throws HibernateException, SQLException {
//        String name = resultSet.getString(names[0]);
//        E result = null;
//        if (!resultSet.wasNull()) {
//            result = Enum.valueOf(clazz, name);
//        }
//        return result;
//    }
//
//    public void nullSafeSet(
//            PreparedStatement preparedStatement,
//            Object value,
//            int index)
//    throws HibernateException, SQLException {
//        if (null == value) {
//            preparedStatement.setNull(index, Types.VARCHAR);
//        } else {
//            preparedStatement.setString(index, ((Enum<?>)value).name());
//        }
//    }

    public Object deepCopy(Object value) throws HibernateException{
        return value;
    }

    public boolean isMutable() {
        return false;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
         return cached;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable)value;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        String name = rs.getString(names[0]);
        E result = null;
        if (!rs.wasNull()) {
            result = Enum.valueOf(clazz, name);
        }
        return result;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (null == value) {
            st.setNull(index, Types.VARCHAR);
        } else {
            st.setString(index, ((Enum<?>)value).name());
        }
    }


//    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        String name = rs.getString(names[0]);
        E result = null;
        if (!rs.wasNull()) {
            result = Enum.valueOf(clazz, name);
        }
        return result;
    }
    
    
//    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (null == value) {
            st.setNull(index, Types.VARCHAR);
        } else {
            st.setString(index, ((Enum<?>)value).name());
        }
    }
    
    
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y)
            return true;
        if (null == x || null == y)
            return false;
        return x.equals(y);
    }
}
