INSERT INTO SERVICE_SPECIFICATION (id, json_id, href, name, version) VALUES (1, '1', 'http://server:port/serviceCatalogManagement/serviceSpecification/1', 'name1', 'version1');
INSERT INTO SERVICE_SPECIFICATION (id, json_id, href, name, version) VALUES (2, '2', 'http://server:port/serviceCatalogManagement/serviceSpecification/2', 'name2', 'version2');

INSERT INTO SERVICE_ORDER (id, json_id, href) VALUES (1, 1, 'href1');
INSERT INTO SERVICE_ORDER (id, json_id, href) VALUES (2, 2, 'href2');

INSERT INTO SERVICE (id, category, description, end_date, has_started, href, is_service_enabled, is_stateful, name, order_date, start_date, start_mode, state, type, service_order_id, service_specification_id) VALUES (1, 'CFS', 'Description of service 1', '2017-11-15T08:22:12+01:00', true, 'http://server:port/serviceInventoryManagement/service/1', true, true, 'Email', '2017-11-15T08:22:12+01:00', '2017-11-15T08:22:12+01:00', 'startMode1', 'state1', 'type1', 2, 1);
INSERT INTO SERVICE (id, category, description, end_date, has_started, href, is_service_enabled, is_stateful, name, order_date, start_date, start_mode, state, type, service_order_id, service_specification_id) VALUES (2, 'RFS', 'Description of service 2', '2017-11-15T08:22:12+01:00', false, 'http://server:port/serviceInventoryManagement/service/2', false, false, 'Search', '2017-11-15T08:22:12+01:00', '2017-11-15T08:22:12+01:00', 'startMode2', 'state2', 'type2', 1, 2);

INSERT INTO RELATED_PARTY (id, json_id, href, name, role, valid_for) VALUES (1, 1, 'href1', 'name1', 'role1', 'validFor1');
INSERT INTO RELATED_PARTY (id, json_id, href, name, role, valid_for) VALUES (2, 2, 'href2', 'name2', 'role2', 'validFor2');

INSERT INTO SERVICE_RELATED_PARTY (service_id, related_party_id) VALUES (1, 1);
INSERT INTO SERVICE_RELATED_PARTY (service_id, related_party_id) VALUES (1, 2);
INSERT INTO SERVICE_RELATED_PARTY (service_id, related_party_id) VALUES (2, 2);

INSERT INTO PLACE (ID, HREF, ROLE, SERVICE_ID) VALUES (1, 'HREF1', 'ROLE1', 1);
INSERT INTO PLACE (ID, HREF, ROLE, SERVICE_ID) VALUES (2, 'HREF2', 'ROLE2', 1);
INSERT INTO PLACE (ID, HREF, ROLE, SERVICE_ID) VALUES (3, 'HREF3', 'ROLE3', 2);

INSERT INTO NOTE (ID, author, date, text, SERVICE_ID) VALUES (1, 'author1', '2017-11-15T08:22:12+01:00','text1', 1);
INSERT INTO NOTE (ID, author, date, text, SERVICE_ID) VALUES (2, 'author2', '2017-11-15T08:22:12+01:00','text2', 1);
INSERT INTO NOTE (ID, author, date, text, SERVICE_ID) VALUES (3, 'author3', '2017-11-15T08:22:13+01:00','text3', 2);

INSERT INTO SERVICE_CHARACTERISTIC (ID, NAME, VALUE, SERVICE_ID) VALUES (1, 'name1', 'value1', 1);
INSERT INTO SERVICE_CHARACTERISTIC (ID, NAME, VALUE, SERVICE_ID) VALUES (2, 'name2', 'value2', 2);
INSERT INTO SERVICE_CHARACTERISTIC (ID, NAME, VALUE, SERVICE_ID) VALUES (3, 'name3', 'value3', 2);

INSERT INTO SERVICE_REF (id, json_id, href) VALUES (1, 1, 'href1');
INSERT INTO SERVICE_REF (id, json_id, href) VALUES (2, 2, 'href2');
INSERT INTO SERVICE_REF (id, json_id, href) VALUES (3, 3, 'href3');

INSERT INTO SERVICE_RELATIONSHIP (ID, TYPE, owning_SERVICE_ID, SERVICE_ID) VALUES (1, 'type1', 1, 1);
INSERT INTO SERVICE_RELATIONSHIP (ID, TYPE, owning_SERVICE_ID, SERVICE_ID) VALUES (2, 'type2', 2, 2);
INSERT INTO SERVICE_RELATIONSHIP (ID, TYPE, owning_SERVICE_ID, SERVICE_ID) VALUES (3, 'type3', 2, 3);

INSERT INTO SUPPORTING_RESOURCE (id, json_id, href, name) VALUES (1, 1, 'href1', 'name1');
INSERT INTO SUPPORTING_RESOURCE (id, json_id, href, name) VALUES (2, 2, 'href2', 'name2');
INSERT INTO SUPPORTING_RESOURCE (id, json_id, href, name) VALUES (3, 3, 'href3', 'name3');

INSERT INTO SERVICE_SUPPORTING_RESOURCE (SERVICE_ID, SUPPORTING_RESOURCE_ID) VALUES (1, 2);
INSERT INTO SERVICE_SUPPORTING_RESOURCE (SERVICE_ID, SUPPORTING_RESOURCE_ID) VALUES (2, 2);
INSERT INTO SERVICE_SUPPORTING_RESOURCE (SERVICE_ID, SUPPORTING_RESOURCE_ID) VALUES (1, 3);
INSERT INTO SERVICE_SUPPORTING_RESOURCE (SERVICE_ID, SUPPORTING_RESOURCE_ID) VALUES (1, 1);
INSERT INTO SERVICE_SUPPORTING_RESOURCE (SERVICE_ID, SUPPORTING_RESOURCE_ID) VALUES (2, 1);

INSERT INTO SUPPORTING_SERVICE (id, json_id, category, href, name) VALUES (1, 1, 'category1', 'href1', 'name1');
INSERT INTO SUPPORTING_SERVICE (id, json_id, category, href, name) VALUES (2, 2, 'category2', 'href2', 'name2');
INSERT INTO SUPPORTING_SERVICE (id, json_id, category, href, name) VALUES (3, 3, 'category3', 'href3', 'name3');

INSERT INTO SERVICE_SUPPORTING_SERVICE (SERVICE_ID, SUPPORTING_SERVICE_ID) VALUES (1, 2);
INSERT INTO SERVICE_SUPPORTING_SERVICE (SERVICE_ID, SUPPORTING_SERVICE_ID) VALUES (2, 2);
INSERT INTO SERVICE_SUPPORTING_SERVICE (SERVICE_ID, SUPPORTING_SERVICE_ID) VALUES (1, 3);
INSERT INTO SERVICE_SUPPORTING_SERVICE (SERVICE_ID, SUPPORTING_SERVICE_ID) VALUES (1, 1);
INSERT INTO SERVICE_SUPPORTING_SERVICE (SERVICE_ID, SUPPORTING_SERVICE_ID) VALUES (2, 1);

