/*
 * Homecenter Envíos - comportamiento del lado del cliente.
 *
 * El JavaScript solo mejora la experiencia de uso; todas las validaciones
 * importantes se repiten en el servidor (LectorFormulario.java), porque el
 * navegador puede tener JavaScript desactivado o ser manipulado.
 */
(function () {
  'use strict';

  /** Abre y cierra el menú lateral en pantallas pequeñas. */
  function configurarMenuMovil() {
    var boton = document.getElementById('botonMenu');
    var menu = document.getElementById('menuLateral');
    if (!boton || !menu) {
      return;
    }
    boton.addEventListener('click', function () {
      var abierto = menu.classList.toggle('abierto');
      boton.setAttribute('aria-expanded', String(abierto));
    });
  }

  /** Botones "MOSTRAR / OCULTAR" junto a los campos de contraseña. */
  function configurarMostrarContrasena() {
    var botones = document.querySelectorAll('.mostrar-contrasena');
    botones.forEach(function (boton) {
      boton.addEventListener('click', function () {
        var campo = document.getElementById(boton.getAttribute('data-campo'));
        var mostrar = campo.type === 'password';
        campo.type = mostrar ? 'text' : 'password';
        boton.textContent = mostrar ? 'OCULTAR' : 'MOSTRAR';
      });
    });
  }

  /**
   * Pide confirmación antes de enviar formularios destructivos.
   * Se activa en cualquier <form> con el atributo data-confirmar="mensaje".
   */
  function configurarConfirmaciones() {
    var formularios = document.querySelectorAll('form[data-confirmar]');
    formularios.forEach(function (formulario) {
      formulario.addEventListener('submit', function (evento) {
        if (!window.confirm(formulario.getAttribute('data-confirmar'))) {
          evento.preventDefault();
        }
      });
    });
  }

  document.addEventListener('DOMContentLoaded', function () {
    configurarMenuMovil();
    configurarMostrarContrasena();
    configurarConfirmaciones();
  });
})();
