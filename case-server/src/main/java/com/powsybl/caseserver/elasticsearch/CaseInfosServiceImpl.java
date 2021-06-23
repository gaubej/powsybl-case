/**
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.powsybl.caseserver.elasticsearch;

import com.google.common.collect.Lists;
import com.powsybl.caseserver.dto.CaseInfos;
import java.util.List;
import java.util.Optional;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;

/**
 * A class to implement metadatas transfer in the DB elasticsearch
 *
 * @author Slimane Amar <slimane.amar at rte-france.com>
 */
@ComponentScan(basePackageClasses = {CaseInfosRepository.class})
public class CaseInfosServiceImpl implements CaseInfosService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaseInfosServiceImpl.class);

    @Autowired
    private CaseInfosRepository caseInfosRepository;

    @Override
    public CaseInfos addCaseInfos(@NonNull final CaseInfos ci) {
        caseInfosRepository.save(ci);
        return ci;
    }

    @Override
    public Optional<CaseInfos> getCaseInfosByUuid(@NonNull final String uuid) {
        Page<CaseInfos> res = caseInfosRepository.findByUuid(uuid,  PageRequest.of(0, 1));
        return res.get().findFirst();
    }

    @Override
    public List<CaseInfos> getAllCaseInfos() {
        return Lists.newArrayList(caseInfosRepository.findAll());
    }

    @Override
    public List<CaseInfos> searchCaseInfos(@NonNull final String query) {
        return Lists.newArrayList(caseInfosRepository.search(QueryBuilders.queryStringQuery(query)));
    }

    @Override
    public void deleteCaseInfos(@NonNull final CaseInfos ci) {
        caseInfosRepository.delete(ci);
    }

    @Override
    public void deleteCaseInfosByUuid(@NonNull  String uuid) {
        caseInfosRepository.deleteById(uuid);
    }

    @Override
    public void deleteAllCaseInfos() {
        //FIXME why does this throw an exception
        //org.springframework.data.elasticsearch.UncategorizedElasticsearchException: Elasticsearch exception [type=illegal_argument_exception, reason=request [/case-server/_delete_by_query] contains unrecognized parameters: [allow_no_indices], [conflicts], [expand_wildcards], [ignore_throttled], [ignore_unavailable], [requests_per_second], [slices], [wait_for_completion]]; nested exception is ElasticsearchStatusException[Elasticsearch exception [type=illegal_argument_exception, reason=request [/case-server/_delete_by_query] contains unrecognized parameters: [allow_no_indices], [conflicts], [expand_wildcards], [ignore_throttled], [ignore_unavailable], [requests_per_second], [slices], [wait_for_completion]]]
        //caseInfosRepository.deleteAll();
        getAllCaseInfos().forEach(caseInfos -> caseInfosRepository.delete(caseInfos));
    }
}
