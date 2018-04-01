/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import progtech2.backend.entities.Order;
import progtech2.backend.entities.OrderLine;
import progtech2.backend.entities.Retailer;
import progtech2.backend.enums.OrderStatus;

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public class RetailerDao extends GenericDao<Retailer, String> implements IRetailerDao {

    public RetailerDao(Connection con) {
        super(con, "retailer", "retailerName");
    }
    
    @Override
    public void delete(String key) {
        String sql = "DELETE FROM \"USERNAME\".\"retailer\" WHERE pname = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, key);

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(GenericDao.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            close(statement, resultSet);
        }
    }

    @Override
    public List<Retailer> findAll() {
        String sql = "SELECT * FROM \"USERNAME\".\"retailer\"";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);

            resultSet = statement.executeQuery();
            List<Retailer> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setRetailer(resultSet));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
        }
        return null;
    }

    @Override
    public Retailer findById(String key) {
        String sql = "SELECT * FROM \"USERNAME\".\"retailer\" WHERE name = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, key);

            resultSet = statement.executeQuery();
            List<Retailer> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setRetailer(resultSet));
            }
            return result.get(0);
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
        }
        return null;
    }

    private Retailer setRetailer(ResultSet resultSet) throws SQLException {
        //address, creditline, name, phone
        Retailer retailer = new Retailer();
        retailer.setName(resultSet.getString("name"));
        retailer.setAddress(resultSet.getString("address"));
        retailer.setCreditLine(resultSet.getBigDecimal("creditLine"));
        retailer.setPhone(resultSet.getString("phone"));
        return retailer;
    }

    @Override
    public Retailer save(Retailer entity) {
        String sql = "INSERT INTO \"USERNAME\".\"retailer\" (name, address, creditLine, phone) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getAddress());
            statement.setBigDecimal(3, entity.getCreditLine());
            statement.setString(4, entity.getPhone());
            statement.executeUpdate();
            return entity;
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
        }
        return null;
    }

    @Override
    public void update(Retailer entity) {
        String sql = "UPDATE \"USERNAME\".\"retailer\" SET address=?, creditLine=?, phone=? WHERE name=?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, entity.getAddress());
            statement.setBigDecimal(2, entity.getCreditLine());
            statement.setString(3, entity.getPhone());
            statement.setString(4, entity.getName());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
        }
    }

    @Override
    public List<Order> findOrdersByRetailerId(String key) {
        String sql = "SELECT * FROM \"USERNAME\".\"order\" WHERE retailerName = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, key);

            resultSet = statement.executeQuery();
            List<Order> result = new LinkedList<>();
            while (resultSet.next()) {
                result.add(setOrder(resultSet));
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(statement, resultSet);
        }
        return null;
    }

    private Order setOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setOrderId(resultSet.getLong("orderId"));
        order.setOrderDate(resultSet.getDate("orderDate"));
        order.setOrderPrice(resultSet.getBigDecimal("orderPrice"));
        order.setStatus(OrderStatus.valueOf(resultSet.getString("status")));
        order.setRetailerName(resultSet.getString("retailerName"));
        return order;
    }

    private void close(PreparedStatement statement, ResultSet resultSet) {
        try {
            if (!(statement == null || statement.isClosed())) {
                statement.close();
            }
            if (!(resultSet == null || resultSet.isClosed())) {
                resultSet.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(RetailerDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}