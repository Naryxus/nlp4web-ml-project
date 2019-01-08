
/* First created by JCasGen Tue Jan 08 14:40:33 CET 2019 */
package types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Tue Jan 08 14:40:33 CET 2019
 * @generated */
public class PhraseAnnotation_Type extends Annotation_Type {
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = PhraseAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("types.PhraseAnnotation");
 
  /** @generated */
  final Feature casFeat_phraseTag;
  /** @generated */
  final int     casFeatCode_phraseTag;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getPhraseTag(int addr) {
        if (featOkTst && casFeat_phraseTag == null)
      jcas.throwFeatMissing("phraseTag", "types.PhraseAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_phraseTag);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPhraseTag(int addr, String v) {
        if (featOkTst && casFeat_phraseTag == null)
      jcas.throwFeatMissing("phraseTag", "types.PhraseAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_phraseTag, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public PhraseAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_phraseTag = jcas.getRequiredFeatureDE(casType, "phraseTag", "uima.cas.String", featOkTst);
    casFeatCode_phraseTag  = (null == casFeat_phraseTag) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_phraseTag).getCode();

  }
}



    