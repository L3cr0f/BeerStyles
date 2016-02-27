/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ingredients;

/**
 *
 * @author ernsferrari
 */
public class Ingredient {
    
    private double wheat;
    private double rye;
    private double rice_corn;
    private double unhopped;
    private double spices;
    
    private String result;
    
    public Ingredient (double wheat, double rye,
            double rice_corn, boolean unhopped, boolean spices){
        
        this.wheat = wheat;
        this.rye = rye;
        this.rice_corn = rice_corn;
        
        if (unhopped) {
            this.unhopped = 1;
        } else {
            this.unhopped = 0;
        }
        
        if (spices) {
            this.spices = 1;
        } else {
            this.spices = 0;
        }
        
        this.result = "Error";
        
    }
    
    public Ingredient (){
        //Constructor por defecto
    }

    public double getWheat() {
        return this.wheat;
    }

    public void setWheat(double wheat) {
        this.wheat = wheat;
    }

    public double getRye() {
        return this.rye;
    }

    public void setRye(double rye) {
        this.rye = rye;
    }

    public double getRice_corn() {
        return this.rice_corn;
    }

    public void setRice_corn(double rice_corn) {
        this.rice_corn = rice_corn;
    }

    public double getUnhopped() {        
        return this.unhopped;
    }

    public void setNo_hop(boolean unhopped) {
        if (unhopped) {
            this.unhopped = 1;
        } else {
            this.unhopped = 0;
        }
    }

    public double getSpices() {
        return this.spices;
    }

    public void setSpices(boolean spices) {
        if (spices) {
            this.spices = 1;
        } else {
            this.spices = 0;
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(double result) {
        if (result == 0) {
            this.result = "Normal";
        } else if (result == 1) {
            this.result = "Trigo";
        } else if (result == 2) {
            this.result = "Centeno";
        } else if (result == 3) {
            this.result = "Arroz/Maiz";
        } else if (result == 4) {
            this.result = "Sin lupulo";
        } else if (result == 5) {
            this.result = "Especias";
        }
    }
    
    
}
