/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beerstyles;

import beerexceptions.ImposibleBeer;

/**
 *
 * @author ernsferrari
 */
public class Beer {

    private double colour;
    private double ibus;
    private double og;
    private double fg;
    private double transparency;
    private double fermentation;
    private double ingredients;
    private double flavor_aroma_1;
    private double flavor_aroma_2;

    private String final_beer;

    public Beer(double colour, double ibus, double og,
            double fg, String transparency, String fermentation,
            String ingredients, String flavor_aroma_1, String flavor_aroma_2) throws ImposibleBeer {

        this.colour = colour;
        this.ibus = ibus;
        this.og = og;
        this.fg = fg;

        if (transparency.equals("Transparente")) {
            this.transparency = 0;
        } else if (transparency.equals("Turbia")) {
            this.transparency = 1;
        } else {
            throw new ImposibleBeer("El valor introducido para la transparecia no existe.");
        }

        if (fermentation.equals("Baja")) {
            this.fermentation = 0;
        } else if (fermentation.equals("Alta")) {
            this.fermentation = 1;
        } else {
            throw new ImposibleBeer("El valor introducido para la fermentacion no existe.");
        }

        if (ingredients.equals("Normal")) {
            this.ingredients = 0;
        } else if (ingredients.equals("Trigo")) {
            this.ingredients = 1;
        } else if (ingredients.equals("Centeno")) {
            this.ingredients = 2;
        } else if (ingredients.equals("Arroz/Maiz")) {
            this.ingredients = 3;
        } else if (ingredients.equals("Sin lupulo")) {
            this.ingredients = 4;
        } else if (ingredients.equals("Especias")) {
            this.ingredients = 5;
        } else {
            throw new ImposibleBeer("El valor introducido para los ingredientes no existe.");
        }

        if (flavor_aroma_1.equals("Fruta (tropical, citricos...)")) {
            this.flavor_aroma_1 = 0;
        } else if (flavor_aroma_1.equals("Herbal")) {
            this.flavor_aroma_1 = 1;
        } else if (flavor_aroma_1.equals("Humo/Quemado")) {
            this.flavor_aroma_1 = 2;
        } else if (flavor_aroma_1.equals("Cafe")) {
            this.flavor_aroma_1 = 3;
        } else if (flavor_aroma_1.equals("Cacao")) {
            this.flavor_aroma_1 = 4;
        } else if (flavor_aroma_1.equals("Caramelo")) {
            this.flavor_aroma_1 = 5;
        } else if (flavor_aroma_1.equals("Cereal")) {
            this.flavor_aroma_1 = 6;
        } else if (flavor_aroma_1.equals("Fruta (ciruela, uva...)")) {
            this.flavor_aroma_1 = 7;
        } else if (flavor_aroma_1.equals("Especias")) {
            this.flavor_aroma_1 = 8;
        } else if (flavor_aroma_1.equals("Mineral")) {
            this.flavor_aroma_1 = 9;
        } else if (flavor_aroma_1.equals("Acido")) {
            this.flavor_aroma_1 = 10;
        } else {
            throw new ImposibleBeer("El valor introducido para los sabores/aromas 1 no existe.");
        }

        if (flavor_aroma_2.equals("Fruta (tropical, citricos...)")) {
            this.flavor_aroma_2 = 0;
        } else if (flavor_aroma_2.equals("Herbal")) {
            this.flavor_aroma_2 = 1;
        } else if (flavor_aroma_2.equals("Humo/Quemado")) {
            this.flavor_aroma_2 = 2;
        } else if (flavor_aroma_2.equals("Cafe")) {
            this.flavor_aroma_2 = 3;
        } else if (flavor_aroma_2.equals("Cacao")) {
            this.flavor_aroma_2 = 4;
        } else if (flavor_aroma_2.equals("Caramelo")) {
            this.flavor_aroma_2 = 5;
        } else if (flavor_aroma_2.equals("Cereal")) {
            this.flavor_aroma_2 = 6;
        } else if (flavor_aroma_2.equals("Fruta (ciruela, uva...)")) {
            this.flavor_aroma_2 = 7;
        } else if (flavor_aroma_2.equals("Especias")) {
            this.flavor_aroma_2 = 8;
        } else if (flavor_aroma_2.equals("Mineral")) {
            this.flavor_aroma_2 = 9;
        } else if (flavor_aroma_2.equals("Acido")) {
            this.flavor_aroma_2 = 10;
        } else {
            throw new ImposibleBeer("El valor introducido para la sabores/aromas 2 no existe.");
        }

        final_beer = "Error";
    }

    public Beer() {
        //Constructor por defecto
    }

    public double getColour() {
        return colour;
    }

    public void setColour(double colour) {
        this.colour = colour;
    }

    public double getIbus() {
        return ibus;
    }

    public void setIbus(double ibus) {
        this.ibus = ibus;
    }

    public double getOg() {
        return og;
    }

    public void setOg(double og) {
        this.og = og;
    }

    public double getFg() {
        return fg;
    }

    public void setFg(double fg) {
        this.fg = fg;
    }

    public double getTransparency() {
        return transparency;
    }

    public void setTransparency(String transparency) throws ImposibleBeer {
        if (transparency.equals("Transparente")) {
            this.transparency = 0;
        } else if (transparency.equals("Turbia")) {
            this.transparency = 1;
        } else {
            throw new ImposibleBeer("El valor introducido para la transparencia no existe.");
        }
    }

    public double getFermentation() {
        return fermentation;
    }

    public void setFermentation(String fermentation) throws ImposibleBeer {
        if (fermentation.equals("Baja")) {
            this.fermentation = 0;
        } else if (fermentation.equals("Alta")) {
            this.fermentation = 1;
        } else {
            throw new ImposibleBeer("El valor introducido para la fermentacion no existe.");
        }
    }

    public double getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) throws ImposibleBeer {
        if (ingredients.equals("Normal")) {
            this.ingredients = 0;
        } else if (ingredients.equals("Trigo")) {
            this.ingredients = 1;
        } else if (ingredients.equals("Centeno")) {
            this.ingredients = 2;
        } else if (ingredients.equals("Arroz/Maiz")) {
            this.ingredients = 3;
        } else if (ingredients.equals("Sin lupulo")) {
            this.ingredients = 4;
        } else if (ingredients.equals("Especias")) {
            this.ingredients = 5;
        } else {
            throw new ImposibleBeer("El valor introducido para los ingredientes no existe.");
        }
    }

    public double getFlavor_aroma_1() {
        return flavor_aroma_1;
    }

    public void setFlavor_aroma_1(String flavor_aroma_1) throws ImposibleBeer {
        if (flavor_aroma_1.equals("Fruta (tropical, citricos...)")) {
            this.flavor_aroma_1 = 0;
        } else if (flavor_aroma_1.equals("Herbal")) {
            this.flavor_aroma_1 = 1;
        } else if (flavor_aroma_1.equals("Humo/Quemado")) {
            this.flavor_aroma_1 = 2;
        } else if (flavor_aroma_1.equals("Cafe")) {
            this.flavor_aroma_1 = 3;
        } else if (flavor_aroma_1.equals("Cacao")) {
            this.flavor_aroma_1 = 4;
        } else if (flavor_aroma_1.equals("Caramelo")) {
            this.flavor_aroma_1 = 5;
        } else if (flavor_aroma_1.equals("Cereal")) {
            this.flavor_aroma_1 = 6;
        } else if (flavor_aroma_1.equals("Fruta (ciruela, uva...)")) {
            this.flavor_aroma_1 = 7;
        } else if (flavor_aroma_1.equals("Especias")) {
            this.flavor_aroma_1 = 8;
        } else if (flavor_aroma_1.equals("Mineral")) {
            this.flavor_aroma_1 = 9;
        } else if (flavor_aroma_1.equals("Acido")) {
            this.flavor_aroma_1 = 10;
        } else {
            throw new ImposibleBeer("El valor introducido para los sabores/aromas 1 no existe.");
        }
    }

    public double getFlavor_aroma_2() {
        return flavor_aroma_2;
    }

    public void setFlavor_aroma_2(String flavor_aroma_2) throws ImposibleBeer {
        if (flavor_aroma_2.equals("Fruta (tropical, citricos...)")) {
            this.flavor_aroma_2 = 0;
        } else if (flavor_aroma_2.equals("Herbal")) {
            this.flavor_aroma_2 = 1;
        } else if (flavor_aroma_2.equals("Humo/Quemado")) {
            this.flavor_aroma_2 = 2;
        } else if (flavor_aroma_2.equals("Cafe")) {
            this.flavor_aroma_2 = 3;
        } else if (flavor_aroma_2.equals("Cacao")) {
            this.flavor_aroma_2 = 4;
        } else if (flavor_aroma_2.equals("Caramelo")) {
            this.flavor_aroma_2 = 5;
        } else if (flavor_aroma_2.equals("Cereal")) {
            this.flavor_aroma_2 = 6;
        } else if (flavor_aroma_2.equals("Fruta (ciruela, uva...)")) {
            this.flavor_aroma_2 = 7;
        } else if (flavor_aroma_2.equals("Especias")) {
            this.flavor_aroma_2 = 8;
        } else if (flavor_aroma_2.equals("Mineral")) {
            this.flavor_aroma_2 = 9;
        } else if (flavor_aroma_2.equals("Acido")) {
            this.flavor_aroma_2 = 10;
        } else {
            throw new ImposibleBeer("El valor introducido para los sabores/aromas 2 no existe.");
        }
    }

    public String getFinal_beer(double final_beer) throws ImposibleBeer {
        if (final_beer == 0) {
            this.final_beer = "Other";
        } else if (final_beer == 1) {
            this.final_beer = "Pale Ale";
        } else if (final_beer == 2) {
            this.final_beer = "IPA";
        } else if (final_beer == 3) {
            this.final_beer = "Imperial IPA";
        } else if (final_beer == 4) {
            this.final_beer = "Black IPA";
        } else if (final_beer == 5) {
            this.final_beer = "Imperial Black IPA";
        } else if (final_beer == 6) {
            this.final_beer = "Stout";
        } else if (final_beer == 7) {
            this.final_beer = "Russian Imperial Stout";
        } else if (final_beer == 8) {
            this.final_beer = "Porter";
        } else if (final_beer == 9) {
            this.final_beer = "Imperial Porter";
        } else if (final_beer == 10) {
            this.final_beer = "Amber Ale";
        } else if (final_beer == 11) {
            this.final_beer = "Barleywine";
        } else if (final_beer == 12) {
            this.final_beer = "Dubbel";
        } else if (final_beer == 13) {
            this.final_beer = "Tripel";
        } else if (final_beer == 14) {
            this.final_beer = "Quadrupel";
        } else if (final_beer == 15) {
            this.final_beer = "Saison";
        } else if (final_beer == 16) {
            this.final_beer = "Witbier";
        } else if (final_beer == 17) {
            this.final_beer = "Hefe Weissbier";
        } else if (final_beer == 18) {
            this.final_beer = "Kristall Weissbier";
        } else if (final_beer == 19) {
            this.final_beer = "Dunkel Weizen";
        } else if (final_beer == 20) {
            this.final_beer = "Weizen Bock";
        } else if (final_beer == 21) {
            this.final_beer = "Scottish Ale";
        } else if (final_beer == 22) {
            this.final_beer = "Scotch Ale / Wee Heavy";
        } else if (final_beer == 23) {
            this.final_beer = "Bitter";
        } else if (final_beer == 24) {
            this.final_beer = "Brown Ale";
        } else if (final_beer == 25) {
            this.final_beer = "Lambic/Sour/Wild Ale";
        } else if (final_beer == 26) {
            this.final_beer = "Rye Ale / Roggenbier";
        } else if (final_beer == 27) {
            this.final_beer = "Cream Ale";
        } else if (final_beer == 28) {
            this.final_beer = "Pale Lager";
        } else if (final_beer == 29) {
            this.final_beer = "Pilsner";
        } else if (final_beer == 30) {
            this.final_beer = "Imperial Pilsner";
        } else if (final_beer == 31) {
            this.final_beer = "IPL";
        } else if (final_beer == 32) {
            this.final_beer = "Imperial IPL";
        } else if (final_beer == 33) {
            this.final_beer = "Black IPL";
        } else if (final_beer == 34) {
            this.final_beer = "Imperial Black IPL";
        } else if (final_beer == 35) {
            this.final_beer = "American Adjunct Lager";
        } else if (final_beer == 36) {
            this.final_beer = "Märzen / Vienna Lager";
        } else if (final_beer == 37) {
            this.final_beer = "Dunkel Lager";
        } else if (final_beer == 38) {
            this.final_beer = "Helles Bock";
        } else if (final_beer == 39) {
            this.final_beer = "Dunkel_Bock";
        } else if (final_beer == 40) {
            this.final_beer = "Doppelbock";
        } else if (final_beer == 41) {
            this.final_beer = "Schwarzbier";
        } else if (final_beer == 42) {
            this.final_beer = "Baltic Porter";
        } else if (final_beer == 43) {
            this.final_beer = "Kellerbier";
        } else if (final_beer == 44) {
            this.final_beer = "Herbed / Spiced Beer";
        } else if (final_beer == 45) {
            this.final_beer = "Smoked Beer";
        } else if (final_beer == 46) {
            this.final_beer = "Gruit Beer";
        } else {
            throw new ImposibleBeer("No éxiste ninguna cerveza con estas características.");
        }

        return this.final_beer;
    }
}
