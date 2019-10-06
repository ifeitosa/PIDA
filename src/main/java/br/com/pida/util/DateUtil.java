/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.pida.util;

/**
 *
 * @author italo
 */
public class DateUtil {
    public static java.sql.Date toDatabase(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
    
    public static java.util.Date fromDatabase(java.sql.Date date) {
        return new java.util.Date(date.getTime());
    }
}
