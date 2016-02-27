/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beerexceptions;

import java.io.IOException;

/**
 *
 * @author ernsferrari
 */
public class ImposibleBeer extends IOException{

    public ImposibleBeer(String msg) {
        super(msg);
    }
}
