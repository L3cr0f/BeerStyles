//++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//     Fuzzy Inference Engine Beer_Styles      //
//++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package beerstyles;

import xfuzzy.FuzzyInferenceEngine;
import xfuzzy.FuzzySingleton;
import xfuzzy.MembershipFunction;

public class Beer_Styles implements FuzzyInferenceEngine {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //      Membership function of an input variable       //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private abstract class InnerMembershipFunction {
  double min, max, step;
  abstract double param(int i);
  double center() { return 0; }
  double basis() { return 0; }
  abstract double isEqual(double x);

  double isSmallerOrEqual(double x) {
   double degree=0, mu;
   for(double y=max; y>=x ; y-=step) if((mu = isEqual(y))>degree) degree=mu;
   return degree;
  }

  double isGreaterOrEqual(double x) {
   double degree=0, mu;
   for(double y=min; y<=x ; y+=step) if((mu = isEqual(y))>degree) degree=mu;
   return degree;
  }

  double isEqual(MembershipFunction mf) {
   if(mf instanceof FuzzySingleton)
    { return isEqual( ((FuzzySingleton) mf).getValue()); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = isEqual(val[i][0]);
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = isEqual(x);
    minmu = (mu1<mu2 ? mu1 : mu2);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isGreaterOrEqual(MembershipFunction mf) {
   if(mf instanceof FuzzySingleton)
    { return isGreaterOrEqual( ((FuzzySingleton) mf).getValue()); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = isGreaterOrEqual(val[i][0]);
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0,greq=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = isEqual(x);
    if( mu2>greq ) greq = mu2;
    if( mu1<greq ) minmu = mu1; else minmu = greq;
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isSmallerOrEqual(MembershipFunction mf) {
   if(mf instanceof FuzzySingleton)
    { return isSmallerOrEqual( ((FuzzySingleton) mf).getValue()); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = isSmallerOrEqual(val[i][0]);
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0,smeq=0;
   for(double x=max; x>=min; x-=step){
    mu1 = mf.compute(x);
    mu2 = isEqual(x);
    if( mu2>smeq ) smeq = mu2;
    if( mu1<smeq ) minmu = mu1; else minmu = smeq;
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isGreater(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.not(isSmallerOrEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.not(isSmallerOrEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,gr,degree=0,smeq=0;
   for(double x=max; x>=min; x-=step){
    mu1 = mf.compute(x);
    mu2 = isEqual(x);
    if( mu2>smeq ) smeq = mu2;
    gr = op.not(smeq);
    minmu = ( mu1<gr ? mu1 : gr);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isSmaller(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.not(isGreaterOrEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.not(isGreaterOrEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,sm,degree=0,greq=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = isEqual(x);
    if( mu2>greq ) greq = mu2;
    sm = op.not(greq);
    minmu = ( mu1<sm ? mu1 : sm);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isNotEqual(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.not(isEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.not(isEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = op.not(isEqual(x));
    minmu = (mu1<mu2 ? mu1 : mu2);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isApproxEqual(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.moreorless(isEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.moreorless(isEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = op.moreorless(isEqual(x));
    minmu = (mu1<mu2 ? mu1 : mu2);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isVeryEqual(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.very(isEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.very(isEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = op.very(isEqual(x));
    minmu = (mu1<mu2 ? mu1 : mu2);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isSlightlyEqual(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.slightly(isEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.slightly(isEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = op.slightly(isEqual(x));
    minmu = (mu1<mu2 ? mu1 : mu2);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //          Abstract class of an operator set          //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private abstract class InnerOperatorset {
  abstract double and(double a, double b);
  abstract double or(double a, double b);
  abstract double also(double a, double b);
  abstract double imp(double a, double b);
  abstract double not(double a);
  abstract double very(double a);
  abstract double moreorless(double a);
  abstract double slightly(double a);
  abstract double defuz(OutputMembershipFunction mf);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //      Class for the conclusion of a fuzzy rule       //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class InnerConclusion {
  private double degree;
  private InnerMembershipFunction mf;
  private InnerOperatorset op;

  InnerConclusion(double degree, InnerMembershipFunction mf, InnerOperatorset op) {
   this.op = op;
   this.degree = degree;
   this.mf = mf;
  }

  public double degree() { return degree; }
  public double compute(double x) { return op.imp(degree,mf.isEqual(x)); }
  public double center() { return mf.center(); }
  public double basis() { return mf.basis(); }
  public double param(int i) { return mf.param(i); }
  public double min() { return mf.min; }
  public double max() { return mf.max; }
  public double step() { return mf.step; }
  public boolean isSingleton() { 
   return mf.getClass().getName().endsWith("MF_xfl_singleton");
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //      Membership function of an output variable      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class OutputMembershipFunction implements MembershipFunction {
  public InnerConclusion[] conc;
  public double[] input;
  private InnerOperatorset op;

  OutputMembershipFunction() {
   this.conc = new InnerConclusion[0];
  }

  public void set(int size, InnerOperatorset op, double[] input) {
   this.input = input;
   this.op = op;
   this.conc = new InnerConclusion[size];
  }

  public void set(int pos, double dg, InnerMembershipFunction imf) {
   this.conc[pos] = new InnerConclusion(dg,imf,op);
  }

  public double compute(double x) {
   double dom = conc[0].compute(x);
   for(int i=1; i<conc.length; i++) dom = op.also(dom,conc[i].compute(x));
   return dom;
  }

  public double defuzzify() {
   return op.defuz(this);
  }

  public double min() {
   return conc[0].min();
  }

  public double max() {
   return conc[0].max();
  }

  public double step() {
   return conc[0].step();
  }

  public boolean isDiscrete() {
   for(int i=0; i<conc.length; i++) if(!conc[i].isSingleton()) return false;
   return true;
  }
 
  public double[][] getDiscreteValues() {
   double[][] value = new double[conc.length][2];
   for(int i=0; i<conc.length; i++) {
    value[i][0] = conc[i].param(0);
    value[i][1] = conc[i].degree();
   }
   return value;
  }

 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //      Membership function MF_xfl_trapezoid      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

  private class MF_xfl_trapezoid extends InnerMembershipFunction {
   double a;
   double b;
   double c;
   double d;

   MF_xfl_trapezoid( double min, double max, double step, double param[]){
    super.min = min;
    super.max = max;
    super.step = step;
    this.a = param[0];
    this.b = param[1];
    this.c = param[2];
    this.d = param[3];
   }
   double param(int _i) {
    switch(_i) {
     case 0: return a;
     case 1: return b;
     case 2: return c;
     case 3: return d;
     default: return 0;
    }
   }
   double isEqual(double x) {
    return (x<a || x>d? 0: (x<b? (x-a)/(b-a) : (x<c?1 : (d-x)/(d-c)))); 
   }
   double isGreaterOrEqual(double x) {
    return (x<a? 0 : (x>b? 1 : (x-a)/(b-a) )); 
   }
   double isSmallerOrEqual(double x) {
    return (x<c? 1 : (x>d? 0 : (d-x)/(d-c) )); 
   }
   double center() {
    return (b+c)/2; 
   }
   double basis() {
    return (d-a); 
   }
  }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //      Membership function MF_xfl_singleton      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

  private class MF_xfl_singleton extends InnerMembershipFunction {
   double a;

   MF_xfl_singleton( double min, double max, double step, double param[]){
    super.min = min;
    super.max = max;
    super.step = step;
    this.a = param[0];
   }
   double param(int _i) {
    switch(_i) {
     case 0: return a;
     default: return 0;
    }
   }
   double isEqual(double x) {
    return (x==a? 1 : 0); 
   }
   double isGreaterOrEqual(double x) {
    return (x>=a? 1 : 0); 
   }
   double isSmallerOrEqual(double x) {
    return (x<=a? 1 : 0); 
   }
   double center() {
    return a; 
   }
  }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //     Operator set OP_OSAlcohol      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class OP_OSAlcohol extends InnerOperatorset {
  double and(double a, double b) {
    return (a<b? a : b); 
  }
  double or(double a, double b) {
    return (a>b? a : b); 
  }
  double also(double a, double b) {
    return (a>b? a : b); 
  }
  double imp(double a, double b) {
    return (a<b? a : b); 
  }
  double not(double a) {
    return 1-a; 
  }
  double very(double a) {
   double w = 2.0;
    return Math.pow(a,w); 
  }
  double moreorless(double a) {
   double w = 0.5;
    return Math.pow(a,w); 
  }
  double slightly(double a) {
    return 4*a*(1-a); 
  }
 double defuz(OutputMembershipFunction mf) {
   double min = mf.min();
   double max = mf.max();
  double num=0, denom=0;
  for(int i=0; i<mf.conc.length; i++) {
   num += mf.conc[i].degree() * mf.conc[i].center() / mf.conc[i].basis();
   denom += mf.conc[i].degree() / mf.conc[i].basis();
  }
  if(denom == 0) return (min+max)/2;
  return num/denom;
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //     Operator set OP_OSAmargor      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class OP_OSAmargor extends InnerOperatorset {
  double and(double a, double b) {
    return (a<b? a : b); 
  }
  double or(double a, double b) {
    return (a>b? a : b); 
  }
  double also(double a, double b) {
    return (a>b? a : b); 
  }
  double imp(double a, double b) {
    return (a<b? a : b); 
  }
  double not(double a) {
    return 1-a; 
  }
  double very(double a) {
   double w = 2.0;
    return Math.pow(a,w); 
  }
  double moreorless(double a) {
   double w = 0.5;
    return Math.pow(a,w); 
  }
  double slightly(double a) {
    return 4*a*(1-a); 
  }
 double defuz(OutputMembershipFunction mf) {
   double min = mf.min();
   double max = mf.max();
  double num=0, denom=0;
  for(int i=0; i<mf.conc.length; i++) {
   num += mf.conc[i].degree() * mf.conc[i].center() / mf.conc[i].basis();
   denom += mf.conc[i].degree() / mf.conc[i].basis();
  }
  if(denom == 0) return (min+max)/2;
  return num/denom;
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //     Operator set OP_OSCerveza      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class OP_OSCerveza extends InnerOperatorset {
  double and(double a, double b) {
    return (a<b? a : b); 
  }
  double or(double a, double b) {
    return (a>b? a : b); 
  }
  double also(double a, double b) {
    return (a>b? a : b); 
  }
  double imp(double a, double b) {
    return (a<b? a : b); 
  }
  double not(double a) {
    return 1-a; 
  }
  double very(double a) {
   double w = 2.0;
    return Math.pow(a,w); 
  }
  double moreorless(double a) {
   double w = 0.5;
    return Math.pow(a,w); 
  }
  double slightly(double a) {
    return 4*a*(1-a); 
  }
 double defuz(OutputMembershipFunction mf) {
   double min = mf.min();
   double max = mf.max();
   double step = mf.step();
  double out=min, maximum = 0;
  for(double x=min; x<=max; x+=step) {
   double m= mf.compute(x);
   if( m > maximum ) { maximum = m; out = x; }
  }
  return out;
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TColor  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TColor {
  private double min = -0.1;
  private double max = 40.0;
  private double step = 1.0025;
  double _p_Dorado[] = { 0.0,1.0,6.0,10.0 };
  double _p_Ambar[] = { 6.0,10.0,14.0,18.0 };
  double _p_Castanio[] = { 14.0,18.0,28.0,32.0 };
  double _p_Negro[] = { 30.0,34.0,40.0,40.1 };
  MF_xfl_trapezoid Dorado = new MF_xfl_trapezoid(min,max,step,_p_Dorado);
  MF_xfl_trapezoid Ambar = new MF_xfl_trapezoid(min,max,step,_p_Ambar);
  MF_xfl_trapezoid Castanio = new MF_xfl_trapezoid(min,max,step,_p_Castanio);
  MF_xfl_trapezoid Negro = new MF_xfl_trapezoid(min,max,step,_p_Negro);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TIBU  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TIBU {
  private double min = 1.0;
  private double max = 120.0;
  private double step = 0.9916666666666667;
  double _p_Bajo[] = { 0.9,1.0,30.0,35.0 };
  double _p_Medio[] = { 30.0,35.0,45.0,50.0 };
  double _p_Alto[] = { 40.0,55.0,120.0,121.0 };
  MF_xfl_trapezoid Bajo = new MF_xfl_trapezoid(min,max,step,_p_Bajo);
  MF_xfl_trapezoid Medio = new MF_xfl_trapezoid(min,max,step,_p_Medio);
  MF_xfl_trapezoid Alto = new MF_xfl_trapezoid(min,max,step,_p_Alto);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TOG  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TOG {
  private double min = 1025.0;
  private double max = 1150.0;
  private double step = 1.0;
  double _p_Baja[] = { 1025.0,1030.0,1055.0,1060.0 };
  double _p_Media[] = { 1050.0,1060.0,1080.0,1090.0 };
  double _p_Alta[] = { 1075.0,1080.0,1100.0,1110.0 };
  double _p_Muy_alta[] = { 1100.0,1110.0,1150.0,1150.1 };
  MF_xfl_trapezoid Baja = new MF_xfl_trapezoid(min,max,step,_p_Baja);
  MF_xfl_trapezoid Media = new MF_xfl_trapezoid(min,max,step,_p_Media);
  MF_xfl_trapezoid Alta = new MF_xfl_trapezoid(min,max,step,_p_Alta);
  MF_xfl_trapezoid Muy_alta = new MF_xfl_trapezoid(min,max,step,_p_Muy_alta);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TFG  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TFG {
  private double min = 1000.0;
  private double max = 1060.0;
  private double step = 1.0;
  double _p_Baja[] = { 999.0,1000.0,1015.0,1020.0 };
  double _p_Media[] = { 1010.0,1015.0,1020.0,1030.0 };
  double _p_Alta[] = { 1020.0,1025.0,1060.0,1061.0 };
  MF_xfl_trapezoid Baja = new MF_xfl_trapezoid(min,max,step,_p_Baja);
  MF_xfl_trapezoid Media = new MF_xfl_trapezoid(min,max,step,_p_Media);
  MF_xfl_trapezoid Alta = new MF_xfl_trapezoid(min,max,step,_p_Alta);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TTransparencia  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TTransparencia {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 1.0;
  double _p_Transparente[] = { 0.0 };
  double _p_Turbia[] = { 1.0 };
  MF_xfl_singleton Transparente = new MF_xfl_singleton(min,max,step,_p_Transparente);
  MF_xfl_singleton Turbia = new MF_xfl_singleton(min,max,step,_p_Turbia);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TFermentacion  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TFermentacion {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 1.0;
  double _p_Baja[] = { 0.0 };
  double _p_Alta[] = { 1.0 };
  MF_xfl_singleton Baja = new MF_xfl_singleton(min,max,step,_p_Baja);
  MF_xfl_singleton Alta = new MF_xfl_singleton(min,max,step,_p_Alta);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TIngredientes  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TIngredientes {
  private double min = 0.0;
  private double max = 5.0;
  private double step = 1.0;
  double _p_Normal[] = { 0.0 };
  double _p_Sin_Lupulo[] = { 1.0 };
  double _p_Trigo[] = { 2.0 };
  double _p_Centeno[] = { 3.0 };
  double _p_Arroz_Maiz[] = { 4.0 };
  double _p_Especias[] = { 5.0 };
  MF_xfl_singleton Normal = new MF_xfl_singleton(min,max,step,_p_Normal);
  MF_xfl_singleton Sin_Lupulo = new MF_xfl_singleton(min,max,step,_p_Sin_Lupulo);
  MF_xfl_singleton Trigo = new MF_xfl_singleton(min,max,step,_p_Trigo);
  MF_xfl_singleton Centeno = new MF_xfl_singleton(min,max,step,_p_Centeno);
  MF_xfl_singleton Arroz_Maiz = new MF_xfl_singleton(min,max,step,_p_Arroz_Maiz);
  MF_xfl_singleton Especias = new MF_xfl_singleton(min,max,step,_p_Especias);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TAroma_Sabor_1  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TAroma_Sabor_1 {
  private double min = 0.0;
  private double max = 10.0;
  private double step = 1.0;
  double _p_Fruta_tropical_citricos[] = { 0.0 };
  double _p_Herbal[] = { 1.0 };
  double _p_Humo_quemado[] = { 2.0 };
  double _p_Cafe[] = { 3.0 };
  double _p_Cacao[] = { 4.0 };
  double _p_Caramelo[] = { 5.0 };
  double _p_Cereal[] = { 6.0 };
  double _p_Fruta_ciruela_uva[] = { 7.0 };
  double _p_Especias[] = { 8.0 };
  double _p_Mineral[] = { 9.0 };
  double _p_Acido[] = { 10.0 };
  MF_xfl_singleton Fruta_tropical_citricos = new MF_xfl_singleton(min,max,step,_p_Fruta_tropical_citricos);
  MF_xfl_singleton Herbal = new MF_xfl_singleton(min,max,step,_p_Herbal);
  MF_xfl_singleton Humo_quemado = new MF_xfl_singleton(min,max,step,_p_Humo_quemado);
  MF_xfl_singleton Cafe = new MF_xfl_singleton(min,max,step,_p_Cafe);
  MF_xfl_singleton Cacao = new MF_xfl_singleton(min,max,step,_p_Cacao);
  MF_xfl_singleton Caramelo = new MF_xfl_singleton(min,max,step,_p_Caramelo);
  MF_xfl_singleton Cereal = new MF_xfl_singleton(min,max,step,_p_Cereal);
  MF_xfl_singleton Fruta_ciruela_uva = new MF_xfl_singleton(min,max,step,_p_Fruta_ciruela_uva);
  MF_xfl_singleton Especias = new MF_xfl_singleton(min,max,step,_p_Especias);
  MF_xfl_singleton Mineral = new MF_xfl_singleton(min,max,step,_p_Mineral);
  MF_xfl_singleton Acido = new MF_xfl_singleton(min,max,step,_p_Acido);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TAroma_Sabor_2  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TAroma_Sabor_2 {
  private double min = 0.0;
  private double max = 10.0;
  private double step = 1.0;
  double _p_Fruta_tropical_citricos[] = { 0.0 };
  double _p_Herbal[] = { 1.0 };
  double _p_Humo_quemado[] = { 2.0 };
  double _p_Cafe[] = { 3.0 };
  double _p_Cacao[] = { 4.0 };
  double _p_Caramelo[] = { 5.0 };
  double _p_Cereal[] = { 6.0 };
  double _p_Fruta_ciruela_uva[] = { 7.0 };
  double _p_Especias[] = { 8.0 };
  double _p_Mineral[] = { 9.0 };
  double _p_Acido[] = { 10.0 };
  MF_xfl_singleton Fruta_tropical_citricos = new MF_xfl_singleton(min,max,step,_p_Fruta_tropical_citricos);
  MF_xfl_singleton Herbal = new MF_xfl_singleton(min,max,step,_p_Herbal);
  MF_xfl_singleton Humo_quemado = new MF_xfl_singleton(min,max,step,_p_Humo_quemado);
  MF_xfl_singleton Cafe = new MF_xfl_singleton(min,max,step,_p_Cafe);
  MF_xfl_singleton Cacao = new MF_xfl_singleton(min,max,step,_p_Cacao);
  MF_xfl_singleton Caramelo = new MF_xfl_singleton(min,max,step,_p_Caramelo);
  MF_xfl_singleton Cereal = new MF_xfl_singleton(min,max,step,_p_Cereal);
  MF_xfl_singleton Fruta_ciruela_uva = new MF_xfl_singleton(min,max,step,_p_Fruta_ciruela_uva);
  MF_xfl_singleton Especias = new MF_xfl_singleton(min,max,step,_p_Especias);
  MF_xfl_singleton Mineral = new MF_xfl_singleton(min,max,step,_p_Mineral);
  MF_xfl_singleton Acido = new MF_xfl_singleton(min,max,step,_p_Acido);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TAlcohol  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TAlcohol {
  private double min = 1.0;
  private double max = 18.0;
  private double step = 0.9444444444444444;
  double _p_Bajo[] = { 0.9,1.0,5.5,6.5 };
  double _p_Medio[] = { 5.0,7.0,10.0,10.5 };
  double _p_Alto[] = { 9.0,10.0,18.0,20.0 };
  MF_xfl_trapezoid Bajo = new MF_xfl_trapezoid(min,max,step,_p_Bajo);
  MF_xfl_trapezoid Medio = new MF_xfl_trapezoid(min,max,step,_p_Medio);
  MF_xfl_trapezoid Alto = new MF_xfl_trapezoid(min,max,step,_p_Alto);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TAmargor  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TAmargor {
  private double min = 0.1;
  private double max = 2.0;
  private double step = 0.04871794871794872;
  double _p_Bajo[] = { 0.1,0.2,0.4,0.5 };
  double _p_Medio[] = { 0.35,0.45,0.6,0.65 };
  double _p_Alto[] = { 0.55,0.65,2.0,2.2375 };
  MF_xfl_trapezoid Bajo = new MF_xfl_trapezoid(min,max,step,_p_Bajo);
  MF_xfl_trapezoid Medio = new MF_xfl_trapezoid(min,max,step,_p_Medio);
  MF_xfl_trapezoid Alto = new MF_xfl_trapezoid(min,max,step,_p_Alto);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TCerveza  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TCerveza {
  private double min = 0.0;
  private double max = 46.0;
  private double step = 1.0;
  double _p_Other[] = { 0.0 };
  double _p_Pale_Ale[] = { 1.0 };
  double _p_IPA[] = { 2.0 };
  double _p_Imperial_IPA[] = { 3.0 };
  double _p_BIPA[] = { 4.0 };
  double _p_Imperial_BIPA[] = { 5.0 };
  double _p_Stout[] = { 6.0 };
  double _p_Imperial_Stout[] = { 7.0 };
  double _p_Porter[] = { 8.0 };
  double _p_Imperial_Porter[] = { 9.0 };
  double _p_Amber_Ale[] = { 10.0 };
  double _p_Barleywine[] = { 11.0 };
  double _p_Dubbel[] = { 12.0 };
  double _p_Tripel[] = { 13.0 };
  double _p_Quadrupel[] = { 14.0 };
  double _p_Saison[] = { 15.0 };
  double _p_Witbier[] = { 16.0 };
  double _p_Hefe_Weissbier[] = { 17.0 };
  double _p_Kristall_Weissbier[] = { 18.0 };
  double _p_Dunkel_Weizen[] = { 19.0 };
  double _p_Weizen_Bock[] = { 20.0 };
  double _p_Scottish_Ale[] = { 21.0 };
  double _p_Scotch_Ale[] = { 22.0 };
  double _p_Bitter[] = { 23.0 };
  double _p_Brown_Ale[] = { 24.0 };
  double _p_Sour_Wild[] = { 25.0 };
  double _p_Rye_Ale[] = { 26.0 };
  double _p_Cream_Ale[] = { 27.0 };
  double _p_Pale_Lager[] = { 28.0 };
  double _p_Pilsner[] = { 29.0 };
  double _p_Imperial_Pilsner[] = { 30.0 };
  double _p_IPL[] = { 31.0 };
  double _p_Imperial_IPL[] = { 32.0 };
  double _p_BIPL[] = { 33.0 };
  double _p_Imperial_BIPL[] = { 34.0 };
  double _p_American_Adjunct_Lager[] = { 35.0 };
  double _p_Vienna_Lager[] = { 36.0 };
  double _p_Dunkel[] = { 37.0 };
  double _p_Helles_Bock[] = { 38.0 };
  double _p_Dunkel_Bock[] = { 39.0 };
  double _p_Doppelbock[] = { 40.0 };
  double _p_Schwarzbier[] = { 41.0 };
  double _p_Baltic_Porter[] = { 42.0 };
  double _p_Kellerbier[] = { 43.0 };
  double _p_Spiced_Beer[] = { 44.0 };
  double _p_Smoked_Beer[] = { 45.0 };
  double _p_Gruit_Beer[] = { 46.0 };
  MF_xfl_singleton Other = new MF_xfl_singleton(min,max,step,_p_Other);
  MF_xfl_singleton Pale_Ale = new MF_xfl_singleton(min,max,step,_p_Pale_Ale);
  MF_xfl_singleton IPA = new MF_xfl_singleton(min,max,step,_p_IPA);
  MF_xfl_singleton Imperial_IPA = new MF_xfl_singleton(min,max,step,_p_Imperial_IPA);
  MF_xfl_singleton BIPA = new MF_xfl_singleton(min,max,step,_p_BIPA);
  MF_xfl_singleton Imperial_BIPA = new MF_xfl_singleton(min,max,step,_p_Imperial_BIPA);
  MF_xfl_singleton Stout = new MF_xfl_singleton(min,max,step,_p_Stout);
  MF_xfl_singleton Imperial_Stout = new MF_xfl_singleton(min,max,step,_p_Imperial_Stout);
  MF_xfl_singleton Porter = new MF_xfl_singleton(min,max,step,_p_Porter);
  MF_xfl_singleton Imperial_Porter = new MF_xfl_singleton(min,max,step,_p_Imperial_Porter);
  MF_xfl_singleton Amber_Ale = new MF_xfl_singleton(min,max,step,_p_Amber_Ale);
  MF_xfl_singleton Barleywine = new MF_xfl_singleton(min,max,step,_p_Barleywine);
  MF_xfl_singleton Dubbel = new MF_xfl_singleton(min,max,step,_p_Dubbel);
  MF_xfl_singleton Tripel = new MF_xfl_singleton(min,max,step,_p_Tripel);
  MF_xfl_singleton Quadrupel = new MF_xfl_singleton(min,max,step,_p_Quadrupel);
  MF_xfl_singleton Saison = new MF_xfl_singleton(min,max,step,_p_Saison);
  MF_xfl_singleton Witbier = new MF_xfl_singleton(min,max,step,_p_Witbier);
  MF_xfl_singleton Hefe_Weissbier = new MF_xfl_singleton(min,max,step,_p_Hefe_Weissbier);
  MF_xfl_singleton Kristall_Weissbier = new MF_xfl_singleton(min,max,step,_p_Kristall_Weissbier);
  MF_xfl_singleton Dunkel_Weizen = new MF_xfl_singleton(min,max,step,_p_Dunkel_Weizen);
  MF_xfl_singleton Weizen_Bock = new MF_xfl_singleton(min,max,step,_p_Weizen_Bock);
  MF_xfl_singleton Scottish_Ale = new MF_xfl_singleton(min,max,step,_p_Scottish_Ale);
  MF_xfl_singleton Scotch_Ale = new MF_xfl_singleton(min,max,step,_p_Scotch_Ale);
  MF_xfl_singleton Bitter = new MF_xfl_singleton(min,max,step,_p_Bitter);
  MF_xfl_singleton Brown_Ale = new MF_xfl_singleton(min,max,step,_p_Brown_Ale);
  MF_xfl_singleton Sour_Wild = new MF_xfl_singleton(min,max,step,_p_Sour_Wild);
  MF_xfl_singleton Rye_Ale = new MF_xfl_singleton(min,max,step,_p_Rye_Ale);
  MF_xfl_singleton Cream_Ale = new MF_xfl_singleton(min,max,step,_p_Cream_Ale);
  MF_xfl_singleton Pale_Lager = new MF_xfl_singleton(min,max,step,_p_Pale_Lager);
  MF_xfl_singleton Pilsner = new MF_xfl_singleton(min,max,step,_p_Pilsner);
  MF_xfl_singleton Imperial_Pilsner = new MF_xfl_singleton(min,max,step,_p_Imperial_Pilsner);
  MF_xfl_singleton IPL = new MF_xfl_singleton(min,max,step,_p_IPL);
  MF_xfl_singleton Imperial_IPL = new MF_xfl_singleton(min,max,step,_p_Imperial_IPL);
  MF_xfl_singleton BIPL = new MF_xfl_singleton(min,max,step,_p_BIPL);
  MF_xfl_singleton Imperial_BIPL = new MF_xfl_singleton(min,max,step,_p_Imperial_BIPL);
  MF_xfl_singleton American_Adjunct_Lager = new MF_xfl_singleton(min,max,step,_p_American_Adjunct_Lager);
  MF_xfl_singleton Vienna_Lager = new MF_xfl_singleton(min,max,step,_p_Vienna_Lager);
  MF_xfl_singleton Dunkel = new MF_xfl_singleton(min,max,step,_p_Dunkel);
  MF_xfl_singleton Helles_Bock = new MF_xfl_singleton(min,max,step,_p_Helles_Bock);
  MF_xfl_singleton Dunkel_Bock = new MF_xfl_singleton(min,max,step,_p_Dunkel_Bock);
  MF_xfl_singleton Doppelbock = new MF_xfl_singleton(min,max,step,_p_Doppelbock);
  MF_xfl_singleton Schwarzbier = new MF_xfl_singleton(min,max,step,_p_Schwarzbier);
  MF_xfl_singleton Baltic_Porter = new MF_xfl_singleton(min,max,step,_p_Baltic_Porter);
  MF_xfl_singleton Kellerbier = new MF_xfl_singleton(min,max,step,_p_Kellerbier);
  MF_xfl_singleton Spiced_Beer = new MF_xfl_singleton(min,max,step,_p_Spiced_Beer);
  MF_xfl_singleton Smoked_Beer = new MF_xfl_singleton(min,max,step,_p_Smoked_Beer);
  MF_xfl_singleton Gruit_Beer = new MF_xfl_singleton(min,max,step,_p_Gruit_Beer);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Rulebase RL_RBAmargor  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private MembershipFunction[] RL_RBAmargor(MembershipFunction IOG, MembershipFunction IIBU) {
  double _rl;
  double _input[] = new double[2];
  if(IOG instanceof FuzzySingleton)
   _input[0] = ((FuzzySingleton) IOG).getValue();
  if(IIBU instanceof FuzzySingleton)
   _input[1] = ((FuzzySingleton) IIBU).getValue();
  OP_OSAmargor _op = new OP_OSAmargor();
  OutputMembershipFunction OAmargor = new OutputMembershipFunction();
  OAmargor.set(12,_op,_input);
  TP_TOG _t_IOG = new TP_TOG();
  TP_TIBU _t_IIBU = new TP_TIBU();
  TP_TAmargor _t_OAmargor = new TP_TAmargor();
  int _i_OAmargor=0;
  _rl = _op.and(_t_IOG.Baja.isEqual(IOG),_t_IIBU.Bajo.isEqual(IIBU));
  OAmargor.set(_i_OAmargor,_rl, _t_OAmargor.Medio); _i_OAmargor++;
  _rl = _op.and(_t_IOG.Baja.isEqual(IOG),_t_IIBU.Medio.isEqual(IIBU));
  OAmargor.set(_i_OAmargor,_rl, _t_OAmargor.Alto); _i_OAmargor++;
  _rl = _op.and(_t_IOG.Baja.isEqual(IOG),_t_IIBU.Alto.isEqual(IIBU));
  OAmargor.set(_i_OAmargor,_rl, _t_OAmargor.Alto); _i_OAmargor++;
  _rl = _op.and(_t_IOG.Media.isEqual(IOG),_t_IIBU.Bajo.isEqual(IIBU));
  OAmargor.set(_i_OAmargor,_rl, _t_OAmargor.Bajo); _i_OAmargor++;
  _rl = _op.and(_t_IOG.Media.isEqual(IOG),_t_IIBU.Medio.isEqual(IIBU));
  OAmargor.set(_i_OAmargor,_rl, _t_OAmargor.Medio); _i_OAmargor++;
  _rl = _op.and(_t_IOG.Media.isEqual(IOG),_t_IIBU.Alto.isEqual(IIBU));
  OAmargor.set(_i_OAmargor,_rl, _t_OAmargor.Alto); _i_OAmargor++;
  _rl = _op.and(_t_IOG.Alta.isEqual(IOG),_t_IIBU.Bajo.isEqual(IIBU));
  OAmargor.set(_i_OAmargor,_rl, _t_OAmargor.Bajo); _i_OAmargor++;
  _rl = _op.and(_t_IOG.Alta.isEqual(IOG),_t_IIBU.Medio.isEqual(IIBU));
  OAmargor.set(_i_OAmargor,_rl, _t_OAmargor.Medio); _i_OAmargor++;
  _rl = _op.and(_t_IOG.Alta.isEqual(IOG),_t_IIBU.Alto.isEqual(IIBU));
  OAmargor.set(_i_OAmargor,_rl, _t_OAmargor.Alto); _i_OAmargor++;
  _rl = _op.and(_t_IOG.Muy_alta.isEqual(IOG),_t_IIBU.Bajo.isEqual(IIBU));
  OAmargor.set(_i_OAmargor,_rl, _t_OAmargor.Bajo); _i_OAmargor++;
  _rl = _op.and(_t_IOG.Muy_alta.isEqual(IOG),_t_IIBU.Medio.isEqual(IIBU));
  OAmargor.set(_i_OAmargor,_rl, _t_OAmargor.Bajo); _i_OAmargor++;
  _rl = _op.and(_t_IOG.Muy_alta.isEqual(IOG),_t_IIBU.Alto.isEqual(IIBU));
  OAmargor.set(_i_OAmargor,_rl, _t_OAmargor.Medio); _i_OAmargor++;
  MembershipFunction[] _output = new MembershipFunction[1];
  _output[0] = new FuzzySingleton(OAmargor.defuzzify());
  return _output;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Rulebase RL_RBAlcohol  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private MembershipFunction[] RL_RBAlcohol(MembershipFunction IOG, MembershipFunction IFG) {
  double _rl;
  double _input[] = new double[2];
  if(IOG instanceof FuzzySingleton)
   _input[0] = ((FuzzySingleton) IOG).getValue();
  if(IFG instanceof FuzzySingleton)
   _input[1] = ((FuzzySingleton) IFG).getValue();
  OP_OSAlcohol _op = new OP_OSAlcohol();
  OutputMembershipFunction OAlcohol = new OutputMembershipFunction();
  OAlcohol.set(12,_op,_input);
  TP_TOG _t_IOG = new TP_TOG();
  TP_TFG _t_IFG = new TP_TFG();
  TP_TAlcohol _t_OAlcohol = new TP_TAlcohol();
  int _i_OAlcohol=0;
  _rl = _op.and(_t_IOG.Baja.isEqual(IOG),_t_IFG.Baja.isEqual(IFG));
  OAlcohol.set(_i_OAlcohol,_rl, _t_OAlcohol.Bajo); _i_OAlcohol++;
  _rl = _op.and(_t_IOG.Baja.isEqual(IOG),_t_IFG.Media.isEqual(IFG));
  OAlcohol.set(_i_OAlcohol,_rl, _t_OAlcohol.Bajo); _i_OAlcohol++;
  _rl = _op.and(_t_IOG.Baja.isEqual(IOG),_t_IFG.Alta.isEqual(IFG));
  OAlcohol.set(_i_OAlcohol,_rl, _t_OAlcohol.Bajo); _i_OAlcohol++;
  _rl = _op.and(_t_IOG.Media.isEqual(IOG),_t_IFG.Baja.isEqual(IFG));
  OAlcohol.set(_i_OAlcohol,_rl, _t_OAlcohol.Medio); _i_OAlcohol++;
  _rl = _op.and(_t_IOG.Media.isEqual(IOG),_t_IFG.Media.isEqual(IFG));
  OAlcohol.set(_i_OAlcohol,_rl, _t_OAlcohol.Medio); _i_OAlcohol++;
  _rl = _op.and(_t_IOG.Media.isEqual(IOG),_t_IFG.Alta.isEqual(IFG));
  OAlcohol.set(_i_OAlcohol,_rl, _t_OAlcohol.Bajo); _i_OAlcohol++;
  _rl = _op.and(_t_IOG.Alta.isEqual(IOG),_t_IFG.Baja.isEqual(IFG));
  OAlcohol.set(_i_OAlcohol,_rl, _t_OAlcohol.Alto); _i_OAlcohol++;
  _rl = _op.and(_t_IOG.Alta.isEqual(IOG),_t_IFG.Media.isEqual(IFG));
  OAlcohol.set(_i_OAlcohol,_rl, _t_OAlcohol.Alto); _i_OAlcohol++;
  _rl = _op.and(_t_IOG.Alta.isEqual(IOG),_t_IFG.Alta.isEqual(IFG));
  OAlcohol.set(_i_OAlcohol,_rl, _t_OAlcohol.Medio); _i_OAlcohol++;
  _rl = _op.and(_t_IOG.Muy_alta.isEqual(IOG),_t_IFG.Baja.isEqual(IFG));
  OAlcohol.set(_i_OAlcohol,_rl, _t_OAlcohol.Alto); _i_OAlcohol++;
  _rl = _op.and(_t_IOG.Muy_alta.isEqual(IOG),_t_IFG.Media.isEqual(IFG));
  OAlcohol.set(_i_OAlcohol,_rl, _t_OAlcohol.Alto); _i_OAlcohol++;
  _rl = _op.and(_t_IOG.Muy_alta.isEqual(IOG),_t_IFG.Alta.isEqual(IFG));
  OAlcohol.set(_i_OAlcohol,_rl, _t_OAlcohol.Alto); _i_OAlcohol++;
  MembershipFunction[] _output = new MembershipFunction[1];
  _output[0] = new FuzzySingleton(OAlcohol.defuzzify());
  return _output;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Rulebase RL_RBCerveza  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private MembershipFunction[] RL_RBCerveza(MembershipFunction IColor, MembershipFunction IAmargor, MembershipFunction IAlcohol, MembershipFunction ITransparencia, MembershipFunction IFermentacion, MembershipFunction IIngredientes, MembershipFunction IAroma_Sabor_1, MembershipFunction IAroma_Sabor_2) {
  double _rl;
  double _input[] = new double[8];
  if(IColor instanceof FuzzySingleton)
   _input[0] = ((FuzzySingleton) IColor).getValue();
  if(IAmargor instanceof FuzzySingleton)
   _input[1] = ((FuzzySingleton) IAmargor).getValue();
  if(IAlcohol instanceof FuzzySingleton)
   _input[2] = ((FuzzySingleton) IAlcohol).getValue();
  if(ITransparencia instanceof FuzzySingleton)
   _input[3] = ((FuzzySingleton) ITransparencia).getValue();
  if(IFermentacion instanceof FuzzySingleton)
   _input[4] = ((FuzzySingleton) IFermentacion).getValue();
  if(IIngredientes instanceof FuzzySingleton)
   _input[5] = ((FuzzySingleton) IIngredientes).getValue();
  if(IAroma_Sabor_1 instanceof FuzzySingleton)
   _input[6] = ((FuzzySingleton) IAroma_Sabor_1).getValue();
  if(IAroma_Sabor_2 instanceof FuzzySingleton)
   _input[7] = ((FuzzySingleton) IAroma_Sabor_2).getValue();
  OP_OSCerveza _op = new OP_OSCerveza();
  OutputMembershipFunction OCerveza = new OutputMembershipFunction();
  OCerveza.set(46,_op,_input);
  TP_TColor _t_IColor = new TP_TColor();
  TP_TAmargor _t_IAmargor = new TP_TAmargor();
  TP_TAlcohol _t_IAlcohol = new TP_TAlcohol();
  TP_TTransparencia _t_ITransparencia = new TP_TTransparencia();
  TP_TFermentacion _t_IFermentacion = new TP_TFermentacion();
  TP_TIngredientes _t_IIngredientes = new TP_TIngredientes();
  TP_TAroma_Sabor_1 _t_IAroma_Sabor_1 = new TP_TAroma_Sabor_1();
  TP_TAroma_Sabor_2 _t_IAroma_Sabor_2 = new TP_TAroma_Sabor_2();
  TP_TCerveza _t_OCerveza = new TP_TCerveza();
  int _i_OCerveza=0;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor))),_t_IAlcohol.Bajo.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Fruta_tropical_citricos.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_tropical_citricos.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_tropical_citricos.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Fruta_tropical_citricos.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Pale_Ale); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_t_IAmargor.Alto.isEqual(IAmargor)),_op.or(_t_IAlcohol.Bajo.isEqual(IAlcohol),_t_IAlcohol.Medio.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Fruta_tropical_citricos.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_tropical_citricos.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_tropical_citricos.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Fruta_tropical_citricos.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.IPA); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_t_IAmargor.Alto.isEqual(IAmargor)),_t_IAlcohol.Alto.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Fruta_tropical_citricos.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_tropical_citricos.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_tropical_citricos.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Fruta_tropical_citricos.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Imperial_IPA); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Negro.isEqual(IColor),_t_IAmargor.Alto.isEqual(IAmargor)),_op.or(_t_IAlcohol.Bajo.isEqual(IAlcohol),_t_IAlcohol.Medio.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cafe.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cafe.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.BIPA); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Negro.isEqual(IColor),_t_IAmargor.Alto.isEqual(IAmargor)),_t_IAlcohol.Alto.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cafe.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cafe.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Imperial_BIPA); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Negro.isEqual(IColor),_op.or(_t_IAmargor.Medio.isEqual(IAmargor),_t_IAmargor.Alto.isEqual(IAmargor))),_op.or(_t_IAlcohol.Bajo.isEqual(IAlcohol),_t_IAlcohol.Medio.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cafe.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Humo_quemado.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Humo_quemado.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cafe.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Stout); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Negro.isEqual(IColor),_op.or(_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor)),_t_IAmargor.Alto.isEqual(IAmargor))),_t_IAlcohol.Alto.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cafe.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Humo_quemado.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Humo_quemado.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cafe.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Imperial_Stout); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Negro.isEqual(IColor),_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor))),_op.or(_t_IAlcohol.Bajo.isEqual(IAlcohol),_t_IAlcohol.Medio.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Cafe.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cacao.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Cacao.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cafe.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cafe.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cafe.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cacao.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cacao.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Porter); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Negro.isEqual(IColor),_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor))),_t_IAlcohol.Alto.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Cafe.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cacao.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Cacao.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cafe.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cafe.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cafe.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cacao.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cacao.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Imperial_Porter); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Ambar.isEqual(IColor),_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor))),_op.or(_t_IAlcohol.Bajo.isEqual(IAlcohol),_t_IAlcohol.Medio.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Fruta_tropical_citricos.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_tropical_citricos.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Amber_Ale); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_t_IColor.Castanio.isEqual(IColor)),_t_IAmargor.Medio.isEqual(IAmargor)),_t_IAlcohol.Alto.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Fruta_ciruela_uva.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_ciruela_uva.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Fruta_ciruela_uva.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_ciruela_uva.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Barleywine); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_t_IColor.Ambar.isEqual(IColor),_t_IColor.Castanio.isEqual(IColor)),_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor))),_t_IAlcohol.Medio.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Fruta_ciruela_uva.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_ciruela_uva.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Especias.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Especias.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Especias.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_ciruela_uva.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Fruta_ciruela_uva.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Especias.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Dubbel); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Dorado.isEqual(IColor),_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor))),_op.or(_t_IAlcohol.Medio.isEqual(IAlcohol),_t_IAlcohol.Alto.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Especias.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Especias.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Tripel); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_t_IColor.Ambar.isEqual(IColor),_t_IColor.Castanio.isEqual(IColor)),_t_IAmargor.Bajo.isEqual(IAmargor)),_t_IAlcohol.Alto.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Fruta_ciruela_uva.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_ciruela_uva.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Especias.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Especias.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Especias.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_ciruela_uva.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Fruta_ciruela_uva.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Especias.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Quadrupel); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Dorado.isEqual(IColor),_t_IAmargor.Bajo.isEqual(IAmargor)),_t_IAlcohol.Bajo.isEqual(IAlcohol)),_t_ITransparencia.Turbia.isEqual(ITransparencia)),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Trigo.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_tropical_citricos.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Fruta_tropical_citricos.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Witbier); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_t_IAmargor.Bajo.isEqual(IAmargor)),_t_IAlcohol.Bajo.isEqual(IAlcohol)),_t_ITransparencia.Turbia.isEqual(ITransparencia)),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Trigo.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Especias.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Especias.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Mineral.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Mineral.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Especias.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Mineral.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Mineral.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Especias.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Hefe_Weissbier); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Dorado.isEqual(IColor),_t_IAmargor.Bajo.isEqual(IAmargor)),_t_IAlcohol.Bajo.isEqual(IAlcohol)),_t_ITransparencia.Transparente.isEqual(ITransparencia)),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Trigo.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Especias.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Especias.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Mineral.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Mineral.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Especias.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Mineral.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Mineral.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Especias.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Kristall_Weissbier); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Castanio.isEqual(IColor),_t_IAmargor.Bajo.isEqual(IAmargor)),_t_IAlcohol.Bajo.isEqual(IAlcohol)),_t_ITransparencia.Turbia.isEqual(ITransparencia)),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Trigo.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Especias.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Especias.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Fruta_ciruela_uva.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_ciruela_uva.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Especias.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_ciruela_uva.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Fruta_ciruela_uva.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Especias.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Dunkel_Weizen); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_t_IColor.Castanio.isEqual(IColor)),_t_IAmargor.Bajo.isEqual(IAmargor)),_op.or(_t_IAlcohol.Medio.isEqual(IAlcohol),_t_IAlcohol.Alto.isEqual(IAlcohol))),_t_ITransparencia.Turbia.isEqual(ITransparencia)),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Trigo.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Especias.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Especias.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Fruta_ciruela_uva.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_ciruela_uva.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Especias.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_ciruela_uva.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Fruta_ciruela_uva.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Especias.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Weizen_Bock); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_t_IColor.Ambar.isEqual(IColor),_t_IColor.Castanio.isEqual(IColor)),_t_IAmargor.Bajo.isEqual(IAmargor)),_t_IAlcohol.Bajo.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Scottish_Ale); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_t_IColor.Ambar.isEqual(IColor),_t_IColor.Castanio.isEqual(IColor)),_t_IAmargor.Bajo.isEqual(IAmargor)),_op.or(_t_IAlcohol.Medio.isEqual(IAlcohol),_t_IAlcohol.Alto.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Scotch_Ale); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_t_IColor.Castanio.isEqual(IColor)),_op.or(_t_IAmargor.Medio.isEqual(IAmargor),_t_IAmargor.Alto.isEqual(IAmargor))),_t_IAlcohol.Bajo.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Bitter); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Castanio.isEqual(IColor),_op.or(_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor)),_t_IAmargor.Alto.isEqual(IAmargor))),_op.or(_t_IAlcohol.Bajo.isEqual(IAlcohol),_t_IAlcohol.Medio.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Cacao.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cacao.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cacao.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cacao.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Brown_Ale); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor))),_op.or(_op.or(_t_IAlcohol.Bajo.isEqual(IAlcohol),_t_IAlcohol.Medio.isEqual(IAlcohol)),_t_IAlcohol.Alto.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_op.or(_t_IIngredientes.Normal.isEqual(IIngredientes),_t_IIngredientes.Trigo.isEqual(IIngredientes))),_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Acido.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Mineral.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Acido.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_tropical_citricos.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Mineral.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Acido.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Fruta_tropical_citricos.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Acido.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Saison); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_op.or(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_t_IColor.Castanio.isEqual(IColor)),_t_IColor.Negro.isEqual(IColor)),_op.or(_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor)),_t_IAmargor.Alto.isEqual(IAmargor))),_op.or(_op.or(_t_IAlcohol.Bajo.isEqual(IAlcohol),_t_IAlcohol.Medio.isEqual(IAlcohol)),_t_IAlcohol.Alto.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_op.or(_t_IIngredientes.Normal.isEqual(IIngredientes),_t_IIngredientes.Trigo.isEqual(IIngredientes))),_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Acido.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Acido.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Acido.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Acido.isNotEqual(IAroma_Sabor_2, _op))),_op.and(_t_IAroma_Sabor_1.Acido.isNotEqual(IAroma_Sabor_1, _op),_t_IAroma_Sabor_2.Acido.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Sour_Wild); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_op.or(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_t_IColor.Castanio.isEqual(IColor)),_t_IColor.Negro.isEqual(IColor)),_op.or(_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor)),_t_IAmargor.Alto.isEqual(IAmargor))),_op.or(_op.or(_t_IAlcohol.Bajo.isEqual(IAlcohol),_t_IAlcohol.Medio.isEqual(IAlcohol)),_t_IAlcohol.Alto.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Centeno.isEqual(IIngredientes));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Rye_Ale); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Dorado.isEqual(IColor),_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor))),_t_IAlcohol.Bajo.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Alta.isEqual(IFermentacion)),_t_IIngredientes.Arroz_Maiz.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Cream_Ale); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Dorado.isEqual(IColor),_t_IAmargor.Bajo.isEqual(IAmargor)),_t_IAlcohol.Bajo.isEqual(IAlcohol)),_t_ITransparencia.Transparente.isEqual(ITransparencia)),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Pale_Lager); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Dorado.isEqual(IColor),_op.or(_t_IAmargor.Medio.isEqual(IAmargor),_t_IAmargor.Alto.isEqual(IAmargor))),_t_IAlcohol.Bajo.isEqual(IAlcohol)),_t_ITransparencia.Transparente.isEqual(ITransparencia)),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Pilsner); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Dorado.isEqual(IColor),_op.or(_t_IAmargor.Medio.isEqual(IAmargor),_t_IAmargor.Alto.isEqual(IAmargor))),_op.or(_t_IAlcohol.Medio.isEqual(IAlcohol),_t_IAlcohol.Alto.isEqual(IAlcohol))),_t_ITransparencia.Transparente.isEqual(ITransparencia)),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Imperial_Pilsner); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_t_IAmargor.Alto.isEqual(IAmargor)),_op.or(_t_IAlcohol.Bajo.isEqual(IAlcohol),_t_IAlcohol.Medio.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Fruta_tropical_citricos.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_tropical_citricos.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_tropical_citricos.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Fruta_tropical_citricos.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.IPL); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_t_IAmargor.Alto.isEqual(IAmargor)),_t_IAlcohol.Alto.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Fruta_tropical_citricos.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_tropical_citricos.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_tropical_citricos.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Fruta_tropical_citricos.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Imperial_IPL); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Negro.isEqual(IColor),_t_IAmargor.Alto.isEqual(IAmargor)),_op.or(_t_IAlcohol.Bajo.isEqual(IAlcohol),_t_IAlcohol.Medio.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cafe.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cafe.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.BIPL); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Negro.isEqual(IColor),_t_IAmargor.Alto.isEqual(IAmargor)),_t_IAlcohol.Alto.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cafe.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cafe.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Imperial_BIPL); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Dorado.isEqual(IColor),_t_IAmargor.Bajo.isEqual(IAmargor)),_t_IAlcohol.Bajo.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Arroz_Maiz.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.American_Adjunct_Lager); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Ambar.isEqual(IColor),_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor))),_t_IAlcohol.Bajo.isEqual(IAlcohol)),_t_ITransparencia.Transparente.isEqual(ITransparencia)),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Vienna_Lager); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Castanio.isEqual(IColor),_t_IAmargor.Bajo.isEqual(IAmargor)),_t_IAlcohol.Bajo.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Dunkel); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor))),_t_IAlcohol.Medio.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Especias.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Especias.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Helles_Bock); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Castanio.isEqual(IColor),_t_IAmargor.Bajo.isEqual(IAmargor)),_t_IAlcohol.Medio.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Dunkel_Bock); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_t_IColor.Castanio.isEqual(IColor),_t_IColor.Negro.isEqual(IColor)),_t_IAmargor.Bajo.isEqual(IAmargor)),_t_IAlcohol.Alto.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Doppelbock); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_t_IColor.Negro.isEqual(IColor),_t_IAmargor.Bajo.isEqual(IAmargor)),_t_IAlcohol.Bajo.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Cafe.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Caramelo.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Caramelo.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cafe.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Schwarzbier); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_t_IColor.Castanio.isEqual(IColor),_t_IColor.Negro.isEqual(IColor)),_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor))),_t_IAlcohol.Alto.isEqual(IAlcohol)),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.or(_op.or(_op.or(_op.or(_op.and(_t_IAroma_Sabor_1.Fruta_ciruela_uva.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cacao.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Cacao.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_ciruela_uva.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cacao.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cafe.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cafe.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Fruta_ciruela_uva.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Cafe.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cacao.isEqual(IAroma_Sabor_2))),_op.and(_t_IAroma_Sabor_1.Fruta_ciruela_uva.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cafe.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Baltic_Porter); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor))),_t_IAlcohol.Bajo.isEqual(IAlcohol)),_t_ITransparencia.Turbia.isEqual(ITransparencia)),_t_IFermentacion.Baja.isEqual(IFermentacion)),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Herbal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Cereal.isEqual(IAroma_Sabor_2)),_op.and(_t_IAroma_Sabor_1.Cereal.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Herbal.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Kellerbier); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_op.or(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_t_IColor.Castanio.isEqual(IColor)),_t_IColor.Negro.isEqual(IColor)),_op.or(_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor)),_t_IAmargor.Alto.isEqual(IAmargor))),_op.or(_op.or(_t_IAlcohol.Bajo.isEqual(IAlcohol),_t_IAlcohol.Medio.isEqual(IAlcohol)),_t_IAlcohol.Alto.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_op.or(_t_IFermentacion.Alta.isEqual(IFermentacion),_t_IFermentacion.Baja.isEqual(IFermentacion))),_t_IIngredientes.Especias.isEqual(IIngredientes));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Spiced_Beer); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_op.or(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_t_IColor.Castanio.isEqual(IColor)),_t_IColor.Negro.isEqual(IColor)),_op.or(_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor)),_t_IAmargor.Alto.isEqual(IAmargor))),_op.or(_op.or(_t_IAlcohol.Bajo.isEqual(IAlcohol),_t_IAlcohol.Medio.isEqual(IAlcohol)),_t_IAlcohol.Alto.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_op.or(_t_IFermentacion.Alta.isEqual(IFermentacion),_t_IFermentacion.Baja.isEqual(IFermentacion))),_t_IIngredientes.Normal.isEqual(IIngredientes)),_op.or(_op.and(_t_IAroma_Sabor_1.Humo_quemado.isEqual(IAroma_Sabor_1),_t_IAroma_Sabor_2.Humo_quemado.isNotEqual(IAroma_Sabor_2, _op)),_op.and(_t_IAroma_Sabor_1.Humo_quemado.isNotEqual(IAroma_Sabor_1, _op),_t_IAroma_Sabor_2.Humo_quemado.isEqual(IAroma_Sabor_2))));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Smoked_Beer); _i_OCerveza++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_op.or(_op.or(_op.or(_t_IColor.Dorado.isEqual(IColor),_t_IColor.Ambar.isEqual(IColor)),_t_IColor.Castanio.isEqual(IColor)),_t_IColor.Negro.isEqual(IColor)),_op.or(_op.or(_t_IAmargor.Bajo.isEqual(IAmargor),_t_IAmargor.Medio.isEqual(IAmargor)),_t_IAmargor.Alto.isEqual(IAmargor))),_op.or(_op.or(_t_IAlcohol.Bajo.isEqual(IAlcohol),_t_IAlcohol.Medio.isEqual(IAlcohol)),_t_IAlcohol.Alto.isEqual(IAlcohol))),_op.or(_t_ITransparencia.Transparente.isEqual(ITransparencia),_t_ITransparencia.Turbia.isEqual(ITransparencia))),_op.or(_t_IFermentacion.Alta.isEqual(IFermentacion),_t_IFermentacion.Baja.isEqual(IFermentacion))),_t_IIngredientes.Sin_Lupulo.isEqual(IIngredientes));
  OCerveza.set(_i_OCerveza,_rl, _t_OCerveza.Gruit_Beer); _i_OCerveza++;
  MembershipFunction[] _output = new MembershipFunction[1];
  _output[0] = new FuzzySingleton(OCerveza.defuzzify());
  return _output;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //               Fuzzy Inference Engine                //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public double[] crispInference(double[] _input) {
  MembershipFunction IColor = new FuzzySingleton(_input[0]);
  MembershipFunction IIBU = new FuzzySingleton(_input[1]);
  MembershipFunction IOG = new FuzzySingleton(_input[2]);
  MembershipFunction IFG = new FuzzySingleton(_input[3]);
  MembershipFunction ITransparencia = new FuzzySingleton(_input[4]);
  MembershipFunction IFermentacion = new FuzzySingleton(_input[5]);
  MembershipFunction IIngredientes = new FuzzySingleton(_input[6]);
  MembershipFunction IAroma_Sabor_1 = new FuzzySingleton(_input[7]);
  MembershipFunction IAroma_Sabor_2 = new FuzzySingleton(_input[8]);
  MembershipFunction OCerveza;
  MembershipFunction iBitterness;
  MembershipFunction iAVB;
  MembershipFunction[] _call;
  _call = RL_RBAmargor(IOG,IIBU); iBitterness=_call[0];
  _call = RL_RBAlcohol(IOG,IFG); iAVB=_call[0];
  _call = RL_RBCerveza(IColor,iBitterness,iAVB,ITransparencia,IFermentacion,IIngredientes,IAroma_Sabor_1,IAroma_Sabor_2); OCerveza=_call[0];
  double _output[] = new double[1];
  if(OCerveza instanceof FuzzySingleton)
   _output[0] = ((FuzzySingleton) OCerveza).getValue();
  else _output[0] = ((OutputMembershipFunction) OCerveza).defuzzify();
  return _output;
 }

 public double[] crispInference(MembershipFunction[] _input) {
  MembershipFunction IColor = _input[0];
  MembershipFunction IIBU = _input[1];
  MembershipFunction IOG = _input[2];
  MembershipFunction IFG = _input[3];
  MembershipFunction ITransparencia = _input[4];
  MembershipFunction IFermentacion = _input[5];
  MembershipFunction IIngredientes = _input[6];
  MembershipFunction IAroma_Sabor_1 = _input[7];
  MembershipFunction IAroma_Sabor_2 = _input[8];
  MembershipFunction OCerveza;
  MembershipFunction iBitterness;
  MembershipFunction iAVB;
  MembershipFunction[] _call;
  _call = RL_RBAmargor(IOG,IIBU); iBitterness=_call[0];
  _call = RL_RBAlcohol(IOG,IFG); iAVB=_call[0];
  _call = RL_RBCerveza(IColor,iBitterness,iAVB,ITransparencia,IFermentacion,IIngredientes,IAroma_Sabor_1,IAroma_Sabor_2); OCerveza=_call[0];
  double _output[] = new double[1];
  if(OCerveza instanceof FuzzySingleton)
   _output[0] = ((FuzzySingleton) OCerveza).getValue();
  else _output[0] = ((OutputMembershipFunction) OCerveza).defuzzify();
  return _output;
 }

 public MembershipFunction[] fuzzyInference(double[] _input) {
  MembershipFunction IColor = new FuzzySingleton(_input[0]);
  MembershipFunction IIBU = new FuzzySingleton(_input[1]);
  MembershipFunction IOG = new FuzzySingleton(_input[2]);
  MembershipFunction IFG = new FuzzySingleton(_input[3]);
  MembershipFunction ITransparencia = new FuzzySingleton(_input[4]);
  MembershipFunction IFermentacion = new FuzzySingleton(_input[5]);
  MembershipFunction IIngredientes = new FuzzySingleton(_input[6]);
  MembershipFunction IAroma_Sabor_1 = new FuzzySingleton(_input[7]);
  MembershipFunction IAroma_Sabor_2 = new FuzzySingleton(_input[8]);
  MembershipFunction OCerveza;
  MembershipFunction iBitterness;
  MembershipFunction iAVB;
  MembershipFunction[] _call;
  _call = RL_RBAmargor(IOG,IIBU); iBitterness=_call[0];
  _call = RL_RBAlcohol(IOG,IFG); iAVB=_call[0];
  _call = RL_RBCerveza(IColor,iBitterness,iAVB,ITransparencia,IFermentacion,IIngredientes,IAroma_Sabor_1,IAroma_Sabor_2); OCerveza=_call[0];
  MembershipFunction _output[] = new MembershipFunction[1];
  _output[0] = OCerveza;
  return _output;
 }

 public MembershipFunction[] fuzzyInference(MembershipFunction[] _input) {
  MembershipFunction IColor = _input[0];
  MembershipFunction IIBU = _input[1];
  MembershipFunction IOG = _input[2];
  MembershipFunction IFG = _input[3];
  MembershipFunction ITransparencia = _input[4];
  MembershipFunction IFermentacion = _input[5];
  MembershipFunction IIngredientes = _input[6];
  MembershipFunction IAroma_Sabor_1 = _input[7];
  MembershipFunction IAroma_Sabor_2 = _input[8];
  MembershipFunction OCerveza;
  MembershipFunction iBitterness;
  MembershipFunction iAVB;
  MembershipFunction[] _call;
  _call = RL_RBAmargor(IOG,IIBU); iBitterness=_call[0];
  _call = RL_RBAlcohol(IOG,IFG); iAVB=_call[0];
  _call = RL_RBCerveza(IColor,iBitterness,iAVB,ITransparencia,IFermentacion,IIngredientes,IAroma_Sabor_1,IAroma_Sabor_2); OCerveza=_call[0];
  MembershipFunction _output[] = new MembershipFunction[1];
  _output[0] = OCerveza;
  return _output;
 }

}
