# ---------------------------------------------------------------------- #
# Script generated with: DeZign for Databases V7.3.5                     #
# Target DBMS:           MySQL 5                                         #
# Project file:          databaseSchema.dez                              #
# Project name:                                                          #
# Author:                                                                #
# Script type:           Database creation script                        #
# Created on:            2013-06-19 12:40                                #
# ---------------------------------------------------------------------- #

# ---------------------------------------------------------------------- #
# Create database                                                             #
# ---------------------------------------------------------------------- #
CREATE DATABASE IF NOT EXISTS `transactionQueueDB` CHARACTER SET `utf8` COLLATE `utf8_unicode_ci`;
USE `transactionQueueDB`;

# ---------------------------------------------------------------------- #
# Add tables                                                             #
# ---------------------------------------------------------------------- #

# ---------------------------------------------------------------------- #
# Add table "TransactionQueue"                                           #
# ---------------------------------------------------------------------- #

CREATE TABLE `TransactionQueue` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `msgFrom` VARCHAR(250) NOT NULL,
    `msgTo` VARCHAR(250) NOT NULL,
    `subject` VARCHAR(250) NOT NULL,
    `date` TIMESTAMP NOT NULL,
    `body` BLOB,
    `title` VARCHAR(250),
    `status` VARCHAR(250) NOT NULL DEFAULT 'READY',
    `transmitTime` TIMESTAMP,
    `errorCondition` VARCHAR(500),
    `uniqueID` VARCHAR(100) NOT NULL,
    CONSTRAINT `PK_TransactionQueue` PRIMARY KEY (`id`),
    CONSTRAINT `TUC_TransactionQueue_1` UNIQUE (`uniqueID`)
);

# ---------------------------------------------------------------------- #
# Add table "TransactionQueryHistory"                                    #
# ---------------------------------------------------------------------- #

CREATE TABLE `TransactionQueryHistory` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `queryType` VARCHAR(250),
    `timestamp` TIMESTAMP,
    CONSTRAINT `PK_TransactionQueryHistory` PRIMARY KEY (`id`)
);

# ---------------------------------------------------------------------- #
# Add table "ReconciliationReportLog"                                    #
# ---------------------------------------------------------------------- #

CREATE TABLE `ReconciliationReportLog` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `subject` VARCHAR(250),
    `distributionList` VARCHAR(500),
    `message` BLOB,
    `timestamp` TIMESTAMP,
    `csv` LONGBLOB,
    CONSTRAINT `PK_ReconciliationReportLog` PRIMARY KEY (`id`)
);
