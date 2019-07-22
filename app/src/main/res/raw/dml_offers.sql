DELETE FROM OFFER;
INSERT INTO OFFER(ID, BANNER_TYPE, LABEL_TEXT, BACKGROUND_SOURCE_PATH, SEARCH_QUERY) VALUES (1, 1, '15% OFF', '/drawable/category_blusas', '1/2'), (2, 1, '10% OFF', '/drawable/category_jaquetas', '3/4'), (3, 2, NULL, '/drawable/bonus_banner', NULL);
DELETE FROM OFFER_TEXT;
INSERT INTO OFFER_TEXT ( OFFER_ID, OFFER_TEXT) VALUES (1, 'Blusinhas'), (1, 'Coleção de verão'), (2, 'Vestidos'), (2, 'Queima de estoque'), (3, 'Frete grátis para as regiões Sul e Sudeste'), (3, 'Primeira compra pagamento à vista'), (3, '3% desconto à vista');