package jporebski.microservices.resource_service;

import jakarta.persistence.*;

@Entity
@Table
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] contents;

    public Resource() {

    }

    /** Constructor for displaying this object without all binary data. */
    public Resource(Integer id) {
        this.id = id;
    }

    /** Constructor for creating new objects and saving them. It's without `id` because this value will be auto-generated. */
    public Resource(byte[] inputFileContents) {
        this.contents = inputFileContents;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

}
