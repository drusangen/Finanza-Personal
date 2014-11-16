-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 16-11-2014 a las 21:17:40
-- Versión del servidor: 5.6.20
-- Versión de PHP: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `fxfinanzas`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria_gastos`
--

CREATE TABLE IF NOT EXISTS `categoria_gastos` (
`idcategoria_gastos` int(10) unsigned NOT NULL,
  `nom_cat_gastos` varchar(250) DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Volcado de datos para la tabla `categoria_gastos`
--

INSERT INTO `categoria_gastos` (`idcategoria_gastos`, `nom_cat_gastos`) VALUES
(1, 'Shopping'),
(2, 'Salud y Familia'),
(3, 'Alimentos'),
(4, 'Gastos Bancararios'),
(5, 'Bencina'),
(6, 'Supermercado'),
(7, 'Gastos Básicos'),
(8, 'Otros');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria_ingresos`
--

CREATE TABLE IF NOT EXISTS `categoria_ingresos` (
`idcategoria_ingresos` int(10) unsigned NOT NULL,
  `nom_cat_ingresos` varchar(250) DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

--
-- Volcado de datos para la tabla `categoria_ingresos`
--

INSERT INTO `categoria_ingresos` (`idcategoria_ingresos`, `nom_cat_ingresos`) VALUES
(1, 'Sueldo'),
(2, 'Pagos por cobrar'),
(3, 'Transferencias'),
(4, 'Cheques'),
(5, 'Devolución de impuesto'),
(6, 'Ventas varias'),
(7, 'Otros');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `gastos`
--

CREATE TABLE IF NOT EXISTS `gastos` (
`idgastos` int(10) unsigned NOT NULL,
  `idcategoria_gastos` int(10) unsigned NOT NULL,
  `idusuarios` int(10) unsigned NOT NULL,
  `idmedio_de_pago` int(10) unsigned NOT NULL,
  `monto` int(11) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=13 ;

--
-- Volcado de datos para la tabla `gastos`
--

INSERT INTO `gastos` (`idgastos`, `idcategoria_gastos`, `idusuarios`, `idmedio_de_pago`, `monto`, `fecha`) VALUES
(1, 1, 1, 1, 856000, '2014-03-05 21:39:14'),
(9, 2, 1, 2, 850000, '2014-01-01 03:19:20'),
(10, 2, 1, 2, 600000, '2014-04-16 03:19:27'),
(11, 1, 1, 1, 50000, '2014-11-16 20:51:07'),
(12, 3, 1, 3, 180000, '2014-11-16 20:51:21');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ingresos`
--

CREATE TABLE IF NOT EXISTS `ingresos` (
`idingresos` int(10) unsigned NOT NULL,
  `idcategoria_ingresos` int(10) unsigned NOT NULL,
  `idusuarios` int(10) unsigned NOT NULL,
  `idmedio_de_pago` int(10) unsigned NOT NULL,
  `monto` int(11) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Volcado de datos para la tabla `ingresos`
--

INSERT INTO `ingresos` (`idingresos`, `idcategoria_ingresos`, `idusuarios`, `idmedio_de_pago`, `monto`, `fecha`) VALUES
(4, 1, 1, 1, 500000, '2014-01-08 23:38:30'),
(5, 1, 1, 1, 800000, '2014-02-01 00:00:00'),
(6, 3, 1, 1, 300000, '2014-06-03 00:00:00'),
(7, 2, 1, 2, 760000, '2014-11-16 20:51:44'),
(8, 5, 1, 5, 50000, '2014-11-16 21:03:08');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `medio_de_pago`
--

CREATE TABLE IF NOT EXISTS `medio_de_pago` (
`idmedio_de_pago` int(10) unsigned NOT NULL,
  `nom_mediopago` varchar(255) DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Volcado de datos para la tabla `medio_de_pago`
--

INSERT INTO `medio_de_pago` (`idmedio_de_pago`, `nom_mediopago`) VALUES
(1, 'Efectivo'),
(2, 'Transferencia'),
(3, 'Cheque'),
(4, 'Deposito'),
(5, 'Otros');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mes`
--

CREATE TABLE IF NOT EXISTS `mes` (
`key` int(11) NOT NULL,
  `mes` varchar(50) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=13 ;

--
-- Volcado de datos para la tabla `mes`
--

INSERT INTO `mes` (`key`, `mes`) VALUES
(1, 'Enero'),
(2, 'Febrero'),
(3, 'Marzo'),
(4, 'Abril'),
(5, 'Mayo'),
(6, 'Junio'),
(7, 'Julio'),
(8, 'Agosto'),
(9, 'Septiembre'),
(10, 'Octubre'),
(11, 'Noviembre'),
(12, 'Diciembre');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo`
--

CREATE TABLE IF NOT EXISTS `tipo` (
`idtipo` int(10) unsigned NOT NULL,
  `descripcion` varchar(45) DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Volcado de datos para la tabla `tipo`
--

INSERT INTO `tipo` (`idtipo`, `descripcion`) VALUES
(1, 'Administrador'),
(2, 'Usuario');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE IF NOT EXISTS `usuarios` (
`idusuarios` int(10) unsigned NOT NULL,
  `tipo_idtipo` int(10) unsigned NOT NULL,
  `email` varchar(70) DEFAULT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `edad` datetime DEFAULT NULL,
  `password` varchar(32) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`idusuarios`, `tipo_idtipo`, `email`, `nombre`, `edad`, `password`) VALUES
(1, 1, 'email@dvdeveloper.com', 'Diego Valladares', '2014-10-05 00:00:00', 'ded1e84187398bd4ade42714fd224811');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categoria_gastos`
--
ALTER TABLE `categoria_gastos`
 ADD PRIMARY KEY (`idcategoria_gastos`);

--
-- Indices de la tabla `categoria_ingresos`
--
ALTER TABLE `categoria_ingresos`
 ADD PRIMARY KEY (`idcategoria_ingresos`);

--
-- Indices de la tabla `gastos`
--
ALTER TABLE `gastos`
 ADD PRIMARY KEY (`idgastos`,`idcategoria_gastos`,`idusuarios`,`idmedio_de_pago`), ADD KEY `fk_gastos_categoria_gastos_idx` (`idcategoria_gastos`), ADD KEY `fk_gastos_usuario1_idx` (`idusuarios`), ADD KEY `fk_gastos_medio_de_pago1_idx` (`idmedio_de_pago`);

--
-- Indices de la tabla `ingresos`
--
ALTER TABLE `ingresos`
 ADD PRIMARY KEY (`idingresos`,`idcategoria_ingresos`), ADD KEY `fk_ingresos_categoria_ingresos1_idx` (`idcategoria_ingresos`), ADD KEY `fk_ingresos_usuario1_idx` (`idusuarios`), ADD KEY `fk_ingresos_medio_de_pago1_idx` (`idmedio_de_pago`);

--
-- Indices de la tabla `medio_de_pago`
--
ALTER TABLE `medio_de_pago`
 ADD PRIMARY KEY (`idmedio_de_pago`);

--
-- Indices de la tabla `mes`
--
ALTER TABLE `mes`
 ADD PRIMARY KEY (`key`);

--
-- Indices de la tabla `tipo`
--
ALTER TABLE `tipo`
 ADD PRIMARY KEY (`idtipo`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
 ADD PRIMARY KEY (`idusuarios`,`tipo_idtipo`), ADD KEY `fk_usuario_tipo1_idx` (`tipo_idtipo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categoria_gastos`
--
ALTER TABLE `categoria_gastos`
MODIFY `idcategoria_gastos` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT de la tabla `categoria_ingresos`
--
ALTER TABLE `categoria_ingresos`
MODIFY `idcategoria_ingresos` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT de la tabla `gastos`
--
ALTER TABLE `gastos`
MODIFY `idgastos` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=13;
--
-- AUTO_INCREMENT de la tabla `ingresos`
--
ALTER TABLE `ingresos`
MODIFY `idingresos` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT de la tabla `medio_de_pago`
--
ALTER TABLE `medio_de_pago`
MODIFY `idmedio_de_pago` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT de la tabla `mes`
--
ALTER TABLE `mes`
MODIFY `key` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=13;
--
-- AUTO_INCREMENT de la tabla `tipo`
--
ALTER TABLE `tipo`
MODIFY `idtipo` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
MODIFY `idusuarios` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `gastos`
--
ALTER TABLE `gastos`
ADD CONSTRAINT `fk_gastos_categoria_gastos` FOREIGN KEY (`idcategoria_gastos`) REFERENCES `categoria_gastos` (`idcategoria_gastos`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_gastos_medio_de_pago1` FOREIGN KEY (`idmedio_de_pago`) REFERENCES `medio_de_pago` (`idmedio_de_pago`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_gastos_usuario1` FOREIGN KEY (`idusuarios`) REFERENCES `usuarios` (`idusuarios`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `ingresos`
--
ALTER TABLE `ingresos`
ADD CONSTRAINT `fk_ingresos_categoria_ingresos1` FOREIGN KEY (`idcategoria_ingresos`) REFERENCES `categoria_ingresos` (`idcategoria_ingresos`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ingresos_medio_de_pago1` FOREIGN KEY (`idmedio_de_pago`) REFERENCES `medio_de_pago` (`idmedio_de_pago`) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_ingresos_usuario1` FOREIGN KEY (`idusuarios`) REFERENCES `usuarios` (`idusuarios`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `usuarios`
--
ALTER TABLE `usuarios`
ADD CONSTRAINT `fk_usuario_tipo1` FOREIGN KEY (`tipo_idtipo`) REFERENCES `tipo` (`idtipo`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
