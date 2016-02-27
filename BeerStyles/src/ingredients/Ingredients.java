//++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//     Fuzzy Inference Engine Ingredients      //
//++++++++++++++++++++++++++++++++++++++++++++++++++++++//

package ingredients;

import xfuzzy.FuzzyInferenceEngine;
import xfuzzy.FuzzySingleton;
import xfuzzy.MembershipFunction;

public class Ingredients implements FuzzyInferenceEngine {

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
 //     Operator set OP_OSIngredientes      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class OP_OSIngredientes extends InnerOperatorset {
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
 //  Type TP_TTrigo  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TTrigo {
  private double min = 0.0;
  private double max = 100.0;
  private double step = 1.0;
  double _p_Bajo[] = { -0.1,0.0,10.0,15.0 };
  double _p_Medio[] = { 10.0,20.0,30.0,35.0 };
  double _p_Alto[] = { 30.0,40.0,100.0,100.1 };
  MF_xfl_trapezoid Bajo = new MF_xfl_trapezoid(min,max,step,_p_Bajo);
  MF_xfl_trapezoid Medio = new MF_xfl_trapezoid(min,max,step,_p_Medio);
  MF_xfl_trapezoid Alto = new MF_xfl_trapezoid(min,max,step,_p_Alto);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TCenteno  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TCenteno {
  private double min = 0.0;
  private double max = 100.0;
  private double step = 1.0;
  double _p_Bajo[] = { -0.1,0.0,10.0,15.0 };
  double _p_Medio[] = { 10.0,20.0,30.0,35.0 };
  double _p_Alto[] = { 30.0,40.0,100.0,100.1 };
  MF_xfl_trapezoid Bajo = new MF_xfl_trapezoid(min,max,step,_p_Bajo);
  MF_xfl_trapezoid Medio = new MF_xfl_trapezoid(min,max,step,_p_Medio);
  MF_xfl_trapezoid Alto = new MF_xfl_trapezoid(min,max,step,_p_Alto);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TArroz_Maiz  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TArroz_Maiz {
  private double min = 0.0;
  private double max = 100.0;
  private double step = 1.0;
  double _p_Bajo[] = { -0.1,0.0,10.0,15.0 };
  double _p_Medio[] = { 10.0,20.0,30.0,35.0 };
  double _p_Alto[] = { 30.0,40.0,100.0,100.1 };
  MF_xfl_trapezoid Bajo = new MF_xfl_trapezoid(min,max,step,_p_Bajo);
  MF_xfl_trapezoid Medio = new MF_xfl_trapezoid(min,max,step,_p_Medio);
  MF_xfl_trapezoid Alto = new MF_xfl_trapezoid(min,max,step,_p_Alto);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TSin_Lupulo  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TSin_Lupulo {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 1.0;
  double _p_NO[] = { 0.0 };
  double _p_SI[] = { 1.0 };
  MF_xfl_singleton NO = new MF_xfl_singleton(min,max,step,_p_NO);
  MF_xfl_singleton SI = new MF_xfl_singleton(min,max,step,_p_SI);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_TEspecias_Otros  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_TEspecias_Otros {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 1.0;
  double _p_NO[] = { 0.0 };
  double _p_SI[] = { 1.0 };
  MF_xfl_singleton NO = new MF_xfl_singleton(min,max,step,_p_NO);
  MF_xfl_singleton SI = new MF_xfl_singleton(min,max,step,_p_SI);
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
 //  Rulebase RL_RBIngredientes  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private MembershipFunction[] RL_RBIngredientes(MembershipFunction ITrigo, MembershipFunction ICenteno, MembershipFunction IArroz_Maiz, MembershipFunction ISin_Lupulo, MembershipFunction IEspecias_Otros) {
  double _rl;
  double _input[] = new double[5];
  if(ITrigo instanceof FuzzySingleton)
   _input[0] = ((FuzzySingleton) ITrigo).getValue();
  if(ICenteno instanceof FuzzySingleton)
   _input[1] = ((FuzzySingleton) ICenteno).getValue();
  if(IArroz_Maiz instanceof FuzzySingleton)
   _input[2] = ((FuzzySingleton) IArroz_Maiz).getValue();
  if(ISin_Lupulo instanceof FuzzySingleton)
   _input[3] = ((FuzzySingleton) ISin_Lupulo).getValue();
  if(IEspecias_Otros instanceof FuzzySingleton)
   _input[4] = ((FuzzySingleton) IEspecias_Otros).getValue();
  OP_OSIngredientes _op = new OP_OSIngredientes();
  OutputMembershipFunction OIngredientes = new OutputMembershipFunction();
  OIngredientes.set(9,_op,_input);
  TP_TTrigo _t_ITrigo = new TP_TTrigo();
  TP_TCenteno _t_ICenteno = new TP_TCenteno();
  TP_TArroz_Maiz _t_IArroz_Maiz = new TP_TArroz_Maiz();
  TP_TSin_Lupulo _t_ISin_Lupulo = new TP_TSin_Lupulo();
  TP_TEspecias_Otros _t_IEspecias_Otros = new TP_TEspecias_Otros();
  TP_TIngredientes _t_OIngredientes = new TP_TIngredientes();
  int _i_OIngredientes=0;
  _rl = _op.and(_op.and(_op.and(_op.and(_t_ITrigo.Bajo.isEqual(ITrigo),_t_ICenteno.Bajo.isEqual(ICenteno)),_t_IArroz_Maiz.Bajo.isEqual(IArroz_Maiz)),_t_ISin_Lupulo.NO.isEqual(ISin_Lupulo)),_t_IEspecias_Otros.NO.isEqual(IEspecias_Otros));
  OIngredientes.set(_i_OIngredientes,_rl, _t_OIngredientes.Normal); _i_OIngredientes++;
  _rl = _t_ISin_Lupulo.SI.isEqual(ISin_Lupulo);
  OIngredientes.set(_i_OIngredientes,_rl, _t_OIngredientes.Sin_Lupulo); _i_OIngredientes++;
  _rl = _t_ITrigo.Alto.isEqual(ITrigo);
  OIngredientes.set(_i_OIngredientes,_rl, _t_OIngredientes.Trigo); _i_OIngredientes++;
  _rl = _op.and(_op.and(_t_ITrigo.Medio.isEqual(ITrigo),_op.or(_t_ICenteno.Medio.isEqual(ICenteno),_t_ICenteno.Bajo.isEqual(ICenteno))),_op.or(_t_IArroz_Maiz.Medio.isEqual(IArroz_Maiz),_t_IArroz_Maiz.Bajo.isEqual(IArroz_Maiz)));
  OIngredientes.set(_i_OIngredientes,_rl, _t_OIngredientes.Trigo); _i_OIngredientes++;
  _rl = _op.and(_op.and(_t_ICenteno.Alto.isEqual(ICenteno),_op.or(_t_ITrigo.Medio.isEqual(ITrigo),_t_ITrigo.Bajo.isEqual(ITrigo))),_op.or(_op.or(_t_IArroz_Maiz.Alto.isEqual(IArroz_Maiz),_t_IArroz_Maiz.Medio.isEqual(IArroz_Maiz)),_t_IArroz_Maiz.Bajo.isEqual(IArroz_Maiz)));
  OIngredientes.set(_i_OIngredientes,_rl, _t_OIngredientes.Centeno); _i_OIngredientes++;
  _rl = _op.and(_op.and(_t_ICenteno.Medio.isEqual(ICenteno),_t_ITrigo.Bajo.isEqual(ITrigo)),_op.or(_t_IArroz_Maiz.Medio.isEqual(IArroz_Maiz),_t_IArroz_Maiz.Bajo.isEqual(IArroz_Maiz)));
  OIngredientes.set(_i_OIngredientes,_rl, _t_OIngredientes.Centeno); _i_OIngredientes++;
  _rl = _op.and(_op.and(_t_IArroz_Maiz.Alto.isEqual(IArroz_Maiz),_op.or(_t_ITrigo.Medio.isEqual(ITrigo),_t_ITrigo.Bajo.isEqual(ITrigo))),_op.or(_t_ICenteno.Medio.isEqual(ICenteno),_t_ICenteno.Bajo.isEqual(ICenteno)));
  OIngredientes.set(_i_OIngredientes,_rl, _t_OIngredientes.Arroz_Maiz); _i_OIngredientes++;
  _rl = _op.and(_op.and(_t_IArroz_Maiz.Medio.isEqual(IArroz_Maiz),_t_ITrigo.Bajo.isEqual(ITrigo)),_t_ICenteno.Bajo.isEqual(ICenteno));
  OIngredientes.set(_i_OIngredientes,_rl, _t_OIngredientes.Arroz_Maiz); _i_OIngredientes++;
  _rl = _t_IEspecias_Otros.SI.isEqual(IEspecias_Otros);
  OIngredientes.set(_i_OIngredientes,_rl, _t_OIngredientes.Especias); _i_OIngredientes++;
  MembershipFunction[] _output = new MembershipFunction[1];
  _output[0] = new FuzzySingleton(OIngredientes.defuzzify());
  return _output;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //               Fuzzy Inference Engine                //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public double[] crispInference(double[] _input) {
  MembershipFunction ITrigo = new FuzzySingleton(_input[0]);
  MembershipFunction ICenteno = new FuzzySingleton(_input[1]);
  MembershipFunction IArroz_Maiz = new FuzzySingleton(_input[2]);
  MembershipFunction ISin_Lupulo = new FuzzySingleton(_input[3]);
  MembershipFunction IEspecias_Otros = new FuzzySingleton(_input[4]);
  MembershipFunction OIngredientes;
  MembershipFunction[] _call;
  _call = RL_RBIngredientes(ITrigo,ICenteno,IArroz_Maiz,ISin_Lupulo,IEspecias_Otros); OIngredientes=_call[0];
  double _output[] = new double[1];
  if(OIngredientes instanceof FuzzySingleton)
   _output[0] = ((FuzzySingleton) OIngredientes).getValue();
  else _output[0] = ((OutputMembershipFunction) OIngredientes).defuzzify();
  return _output;
 }

 public double[] crispInference(MembershipFunction[] _input) {
  MembershipFunction ITrigo = _input[0];
  MembershipFunction ICenteno = _input[1];
  MembershipFunction IArroz_Maiz = _input[2];
  MembershipFunction ISin_Lupulo = _input[3];
  MembershipFunction IEspecias_Otros = _input[4];
  MembershipFunction OIngredientes;
  MembershipFunction[] _call;
  _call = RL_RBIngredientes(ITrigo,ICenteno,IArroz_Maiz,ISin_Lupulo,IEspecias_Otros); OIngredientes=_call[0];
  double _output[] = new double[1];
  if(OIngredientes instanceof FuzzySingleton)
   _output[0] = ((FuzzySingleton) OIngredientes).getValue();
  else _output[0] = ((OutputMembershipFunction) OIngredientes).defuzzify();
  return _output;
 }

 public MembershipFunction[] fuzzyInference(double[] _input) {
  MembershipFunction ITrigo = new FuzzySingleton(_input[0]);
  MembershipFunction ICenteno = new FuzzySingleton(_input[1]);
  MembershipFunction IArroz_Maiz = new FuzzySingleton(_input[2]);
  MembershipFunction ISin_Lupulo = new FuzzySingleton(_input[3]);
  MembershipFunction IEspecias_Otros = new FuzzySingleton(_input[4]);
  MembershipFunction OIngredientes;
  MembershipFunction[] _call;
  _call = RL_RBIngredientes(ITrigo,ICenteno,IArroz_Maiz,ISin_Lupulo,IEspecias_Otros); OIngredientes=_call[0];
  MembershipFunction _output[] = new MembershipFunction[1];
  _output[0] = OIngredientes;
  return _output;
 }

 public MembershipFunction[] fuzzyInference(MembershipFunction[] _input) {
  MembershipFunction ITrigo = _input[0];
  MembershipFunction ICenteno = _input[1];
  MembershipFunction IArroz_Maiz = _input[2];
  MembershipFunction ISin_Lupulo = _input[3];
  MembershipFunction IEspecias_Otros = _input[4];
  MembershipFunction OIngredientes;
  MembershipFunction[] _call;
  _call = RL_RBIngredientes(ITrigo,ICenteno,IArroz_Maiz,ISin_Lupulo,IEspecias_Otros); OIngredientes=_call[0];
  MembershipFunction _output[] = new MembershipFunction[1];
  _output[0] = OIngredientes;
  return _output;
 }

}
