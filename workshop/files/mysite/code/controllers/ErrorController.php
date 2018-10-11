<?php

use Psr\Log\LoggerInterface;
use SilverStripe\Control\Controller;
use SilverStripe\Control\HTTPResponse;

class ErrorController extends Controller {
  private static $dependencies = [
    'logger' => '%$' . LoggerInterface::class
  ];

  public $logger;

  private static $allowed_actions = [
    'index',
    'server',
    'client',
    'exception',
  ];

  public function index(){
    $this->logger->warning('Something is causing a warning 🚧');
    parent::init();
  }

  public function server(){
    user_error("Server error 😱", E_USER_WARNING);
    return;
  }

  public function client(){
    $this->setResponse(new HTTPResponse());
    $this->getResponse()->setStatusCode(400);
    $this->getResponse()->setBody('Invalid user interaction 🤯');
    $this->logger->debug('Invalid user interaction 🤯');
    return $this->getResponse();
  }

  public function exception(){
    throw new \LogicException('Welcome to exception land 🔥');
  }
}
