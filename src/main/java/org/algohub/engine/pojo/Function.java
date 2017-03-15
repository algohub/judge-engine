package org.algohub.engine.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.algohub.engine.type.TypeNode;


/**
 * Function metadata.
 */
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.UnusedPrivateField", "PMD.SingularField",
    "PMD.ArrayIsStoredDirectly"}) @JsonIgnoreProperties(ignoreUnknown = true) public
class Function {

  /**
   * Function name.
   */
   private final String name;
  /**
   * Return metadata.
   */
  @JsonProperty("return") private final Return return_;
  /**
   * Parameters' metadata.
   */
  private final Parameter[] parameters;

  /**
   * Since this class is immutable, need to provide a method for Jackson.
   */
  @JsonCreator public Function(@JsonProperty("name") final String name,
      @JsonProperty("return") final Return return_,
      @JsonProperty("parameters") final Parameter[] parameters) {
    this.name = name;
    this.return_ = return_;
    this.parameters = parameters;
  }


  /**
   * Return type.
   */
  @JsonIgnoreProperties(ignoreUnknown = true) public static class Return {
    /**
     * Return data type.
     */
    private final TypeNode type;
    /**
     * Comment of returned value.
     */
    private final String comment;

    /**
     * Since this class is immutable, need to provide a method for Jackson.
     */
    @JsonCreator public Return(@JsonProperty("type") final TypeNode type,
        @JsonProperty("comment") final String comment) {
      this.type = type;
      this.comment = comment;
    }

    public TypeNode getType() {
      return type;
    }

    public String getComment() {
      return comment;
    }
  }


  /**
   * Function parameters' metadata.
   */
  @JsonIgnoreProperties(ignoreUnknown = true) public static class Parameter {
    /**
     * Parameter name.
     */
    private final String name;
    /**
     * Parameter type.
     */
    private final TypeNode type;
    /**
     * Parameter comment.
     */
    private final String comment;

    /**
     * Since this class is immutable, need to provide a method for Jackson.
     */
    @JsonCreator public Parameter(@JsonProperty("name") final String name,
        @JsonProperty("type") final TypeNode type, @JsonProperty("comment") final String comment) {
      this.name = name;
      this.type = type;
      this.comment = comment;
    }

    public String getName() {
      return name;
    }

    public TypeNode getType() {
      return type;
    }

    public String getComment() {
      return comment;
    }
  }

  public String getName() {
    return name;
  }

  public Return getReturn_() {
    return return_;
  }

  public Parameter[] getParameters() {
    return parameters;
  }
}
