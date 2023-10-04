package com.delight.assets.service.provider;

import com.delight.assets.api.model.FileInfo;
import com.delight.assets.dao.entity.UploadConfigEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

public interface StorageProvider {
    Mono<String> save(Flux<DataBuffer> dataBuffer, FileInfo fileInfo, UploadConfigEntity uploadConfigEntity) throws Exception;

    List<String> getPrefixLinks(List<UploadConfigEntity> uploadConfigEntity);

    String getPrefixLink(UploadConfigEntity uploadConfigEntity);
    Mono<Void> delete(UploadConfigEntity uploadConfigEntity,String file);
    Mono<Void> delete(UploadConfigEntity uploadConfigEntity, Collection<String> file);

    ReadResult read(String uri) throws Exception;

    @Setter
    @Getter
    @Accessors(chain = true)
    public class ReadResult {
        Flux<DataBuffer> dataBuffer;
        String contentType;

    }

}
