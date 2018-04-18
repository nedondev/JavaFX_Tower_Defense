/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.loadsave;

/**
 *
 * @author miner
 */
public interface Function<T> {
    public void save(T data)throws Exception;
    public T load()throws Exception;
    
}
