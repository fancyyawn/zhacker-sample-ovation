package top.zhacker.ddd.agilepm.domain.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import top.zhacker.core.model.AssertionConcern;


/**
 * Created by zhacker.
 * Time 2018/6/20 下午9:24
 */
@ToString
@EqualsAndHashCode(callSuper = false)
public class ProductId extends AssertionConcern {
  
  @Getter
  private String id;
  
  private ProductId() {
    super();
  }
  
  public ProductId(String anId) {
    this();
    
    this.setId(anId);
  }
  
  public ProductId(ProductId aProductId) {
    this(aProductId.getId());
  }
  
  
  private void setId(String anId) {
    this.assertArgumentNotEmpty(anId, "The id must be provided.");
    this.assertArgumentLength(anId, 36, "The id must be 36 characters or less.");
    
    this.id = anId;
  }
  
  public String id(){
    return id;
  }
}
