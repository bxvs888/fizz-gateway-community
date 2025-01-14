/*
 *  Copyright (C) 2020 the original author or authors.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package we.plugin.requestbody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import we.flume.clients.log4j2appender.LogService;
import we.plugin.FizzPluginFilter;
import we.plugin.FizzPluginFilterChain;
import we.spring.http.server.reactive.ext.FizzServerHttpRequestDecorator;
import we.util.NettyDataBufferUtils;

import java.util.Map;

/**
 * @author hongqiaowei
 */

@Component(RequestBodyPlugin.REQUEST_BODY_PLUGIN)
public class RequestBodyPlugin implements FizzPluginFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestBodyPlugin.class);

    public static final String REQUEST_BODY_PLUGIN = "requestBodyPlugin";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, Map<String, Object> config) {

        ServerHttpRequest req = exchange.getRequest();
        return
                NettyDataBufferUtils.join(req.getBody()).defaultIfEmpty(NettyDataBufferUtils.EMPTY_DATA_BUFFER)
                        .flatMap(
                                body -> {
                                    ServerWebExchange newExchange = exchange;
                                    if (body != NettyDataBufferUtils.EMPTY_DATA_BUFFER) {
                                        FizzServerHttpRequestDecorator requestDecorator = new FizzServerHttpRequestDecorator(req);
                                        try {
                                            requestDecorator.setBody(body);
                                        } finally {
                                            NettyDataBufferUtils.release(body);
                                        }
                                        newExchange = exchange.mutate().request(requestDecorator).build();
                                        if (log.isDebugEnabled()) {
                                            log.debug("retain body", LogService.BIZ_ID, req.getId());
                                        }
                                    }
                                    return FizzPluginFilterChain.next(newExchange);
                                }
                        );
    }
}
