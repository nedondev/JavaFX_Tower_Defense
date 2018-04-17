/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.loadsave;

import java.io.IOException;

/**
 *
 * @author miner
 */
public interface function<T> {
    public void save(T data)throws IOException;
    public T load()throws IOException;
    
}
