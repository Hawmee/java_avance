-- CreateTable
CREATE TABLE `Enseignants` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `matricule` INTEGER NOT NULL,
    `nom` VARCHAR(191) NOT NULL,
    `taux_horaire` INTEGER NOT NULL,
    `nombre_heure` INTEGER NOT NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
